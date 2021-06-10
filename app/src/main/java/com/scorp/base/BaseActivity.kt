package com.scorp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity <VM: BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: DB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, provideLayoutResId())
        val viewModel = bindViewModel(binding)
        observeProgressLiveData(viewModel)
    }

    private fun observeProgressLiveData(viewModel: VM) {
        viewModel.progressLiveData.observe(this) {
            if(it == true) {
                showLoading(false)
            } else {
                hideLoading(false)
            }
        }
        viewModel.progressSilentLiveData.observe(this) {
            if(it == true) {
                showLoading(true)
            } else {
                hideLoading(true)
            }
        }
    }

    abstract fun showLoading(isSilent: Boolean)

    abstract fun hideLoading(isSilent: Boolean)

    abstract fun provideLayoutResId(): Int

    abstract fun bindViewModel(db: DB): VM
}