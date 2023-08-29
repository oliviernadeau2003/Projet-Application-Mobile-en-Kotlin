package ca.qc.cstj.tenretni.ui.loading

sealed class LoadingUiState {
    object Empty: LoadingUiState()
    class Working(val progression: Int): LoadingUiState()
    object Finished: LoadingUiState()
}