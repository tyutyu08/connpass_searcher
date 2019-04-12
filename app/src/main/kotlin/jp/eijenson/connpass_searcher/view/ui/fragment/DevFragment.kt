package jp.eijenson.connpass_searcher.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.di.module.ViewModelModule
import jp.eijenson.connpass_searcher.domain.repository.DevLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.FavoriteLocalRepository
import jp.eijenson.connpass_searcher.domain.repository.SearchHistoryLocalRepository
import jp.eijenson.connpass_searcher.infra.repository.firebase.RemoteConfigRepository
import jp.eijenson.connpass_searcher.view.livedata.SingleLiveEvent
import jp.eijenson.connpass_searcher.view.presenter.NotificationPresenter
import kotlinx.android.synthetic.main.page_develop.view.*
import javax.inject.Inject

class DevFragment : Fragment() {
    @Inject
    lateinit var model: DevViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.app.appComponent.plus(ViewModelModule(this))
            .inject(this)

        model.log.observe(this, Observer {
            showLog(it)
        })
        model.message.observe(this, Observer {
            showToast(it)
        })
        model.finish.observe(this, Observer {
            activity?.finish()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.page_develop, container, false)
        initView(view)
        return view
    }

    private fun initView(view:View) {
        view.btn_dev_delete?.setOnClickListener {
            model.onClickDataDelete()
        }
        view.btn_dev_switch_api?.setOnClickListener {
            model.onClickDevSwitchApi()
        }
        view.btn_dev_notification?.setOnClickListener {
            NotificationPresenter(requireContext()).notifyNewArrival(3857, "テスト", 999)
            NotificationPresenter(requireContext()).notifyNewArrival(4324, "メルカリ", 431)
        }

        view.btn_dev_remote_config?.setOnClickListener {
            model.onClickRemoteConfig()
        }
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    private fun showLog(log: String) {
        view?.tv_dev_1?.text = log
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}

class DevViewModel(
    private val devLocalRepository: DevLocalRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val searchHistoryLocalRepository: SearchHistoryLocalRepository,
    private val favoriteLocalRepository: FavoriteLocalRepository
) : ViewModel() {
    private val _log = MutableLiveData<String>()
    val log: LiveData<String> = _log
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _finish = SingleLiveEvent<Unit>()
    val finish = _finish

    class Factory(
        private val devLocalRepository: DevLocalRepository,
        private val remoteConfigRepository: RemoteConfigRepository,
        private val searchHistoryLocalRepository: SearchHistoryLocalRepository,
        private val favoriteLocalRepository: FavoriteLocalRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DevViewModel(
                devLocalRepository,
                remoteConfigRepository,
                searchHistoryLocalRepository,
                favoriteLocalRepository
            ) as T
        }
    }

    fun onResume() {
        val text = devLocalRepository.getLog()
        _log.postValue(text)
    }

    fun onClickDataDelete() {
        searchHistoryLocalRepository.deleteAll()
        favoriteLocalRepository.deleteAll()
        devLocalRepository.clear()
        _finish.call()
    }

    fun onClickDevSwitchApi() {
        //
    }

    fun onClickRemoteConfig() {
        remoteConfigRepository.fetch()
        _message.postValue(remoteConfigRepository.getWelcomeMessage())
    }
}