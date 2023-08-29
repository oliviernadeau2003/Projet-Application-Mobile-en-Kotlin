package ca.qc.cstj.tenretni.ui.loading

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import ca.qc.cstj.tenretni.core.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingViewModel: ViewModel() {
    private val _loadingUiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Empty)
    val loadingUiState = _loadingUiState.asStateFlow()

    private var _timerCounter : Int = 0

    private val timer = object: CountDownTimer(Constants.LoadingTimer.TIMER_TIME, Constants.LoadingTimer.TIMER_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            _timerCounter++
            _loadingUiState.update {
                LoadingUiState.Working(_timerCounter)
            }
        }

        override fun onFinish() {
            cancel()
            _loadingUiState.update {
                LoadingUiState.Finished
            }
        }
    }

    fun startTimer() {
        _timerCounter = 0
        timer.start()
    }

}