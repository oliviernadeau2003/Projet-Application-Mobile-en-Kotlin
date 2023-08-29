package ca.qc.cstj.tenretni.ui.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import ca.qc.cstj.tenretni.MainActivity
import ca.qc.cstj.tenretni.R
import ca.qc.cstj.tenretni.databinding.ActivityLoadingBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoadingActivity : AppCompatActivity() {

    private val _viewModel : LoadingViewModel by viewModels()
    private val _binding : ActivityLoadingBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        _viewModel.startTimer()

        _viewModel.loadingUiState.onEach {
            when(it) {
                LoadingUiState.Empty -> Unit
                LoadingUiState.Finished -> {
                    startActivity(MainActivity.newIntent(this))
                    this.finish()
                }
                is LoadingUiState.Working -> {
                    _binding.tvLoading.text = getString(R.string.loading_progression, it.progression)
                    _binding.pgbLoading.setProgress(it.progression, true)
                }
            }
        }.launchIn(lifecycleScope)



    }
}