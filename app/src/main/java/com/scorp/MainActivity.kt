package com.scorp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scorp.base.BaseActivity
import com.scorp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.set


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: MainViewModel by viewModels()

    private val adapter = NameAdapter(emptyList())

    private var alert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.listView.layoutManager = linearLayoutManager
        binding.listView.adapter = adapter

        binding.refreshLayout.setOnRefreshListener(this)
        addScrollListener()

        observeNamesInitialLiveData()
        observeNamesErrorLiveData()

        viewModel.getNames(null)

    }

    private fun addScrollListener() {
        binding.listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && (alert == null || alert?.isShowing == false) ) { // down
                    viewModel.getNamesSilently(viewModel.next)
                }
            }
        })
    }

    private fun observeNamesInitialLiveData() {
        viewModel.namesLiveData.observe(this) {

            if(it.people.isEmpty() && it.next == null && adapter.list.isNotEmpty()) {
                showDialog("No data retrieved, pull down to retry") {}
            } else if(it.people.isEmpty() && it.next == null && adapter.list.isEmpty()) {
                showDialog("No data retrieved, click OK to refresh") {
                    onRefresh()
                }
            }

            viewModel.next = it.next
            it.people.forEach { person ->
                viewModel.peopleMap[person.id] = person
            }

            adapter.list = viewModel.peopleMap.values.toList()
            adapter.notifyDataSetChanged()

            if(adapter.list.isEmpty()) {
                viewModel.emptyListObservable.set(View.VISIBLE)
            }
        }
    }

    private fun showDialog(msg: String, func: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> func.invoke()}
        alert = builder.create()
        alert?.show()

    }

    private fun observeNamesErrorLiveData() {
        viewModel.namesErrorLiveData.observe(this) {
            showDialog("$it, click OK to retry") {
                viewModel.getNamesSilently(viewModel.next)
            }
        }
    }

    override fun showLoading(isSilent: Boolean) {
        if(isSilent) {
            viewModel.loadingObservable.set(View.VISIBLE)
        } else {
            viewModel.progressBarObservable.set(View.VISIBLE)
        }
    }

    override fun hideLoading(isSilent: Boolean) {
        if(isSilent) {
            viewModel.loadingObservable.set(View.GONE)
        } else {
            viewModel.progressBarObservable.set(View.GONE)
        }
    }

    override fun provideLayoutResId() = R.layout.activity_main

    override fun bindViewModel(db: ActivityMainBinding): MainViewModel {
        return viewModel.apply {
            db.viewModel = this
        }
    }

    override fun onRefresh() {
        viewModel.peopleMap.clear()
        viewModel.next = null
        viewModel.emptyListObservable.set(View.GONE)
        refreshList()
    }

    private fun refreshList() {
        viewModel.getNames(null)
        binding.refreshLayout.isRefreshing = false
    }
}