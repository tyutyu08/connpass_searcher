package jp.eijenson.connpass_searcher.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import jp.eijenson.connpass_searcher.App
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.di.module.ViewModelModule
import jp.eijenson.connpass_searcher.view.data.ViewEvent
import jp.eijenson.connpass_searcher.view.data.mapping.toViewEventList
import jp.eijenson.connpass_searcher.view.ui.adapter.EventListAdapter
import jp.eijenson.connpass_searcher.view.ui.container.viewmodel.FavoriteViewModel
import jp.eijenson.model.list.FavoriteList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.page_favorite_list.view.*
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    @Inject
    lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.app.appComponent.plus(ViewModelModule(this))
                .inject(this)
        viewModel.favoriteList.observe(this, Observer {
            showFavoriteList(it)
        })
        viewModel.onCreate()
    }

    fun showFavoriteList(favoriteList: FavoriteList) {
        val adapter = object : EventListAdapter(requireContext(), favoriteList.toViewEventList().toMutableList()) {
            override fun onFavoriteChange(favorite: Boolean, item: ViewEvent) {
                viewModel.changedFavorite(favorite, item)
            }
        }

        val listFavorite = page?.list_favorite
        listFavorite?.adapter = adapter
        listFavorite?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
                requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager(requireContext()).orientation
        )
        listFavorite?.addItemDecoration(dividerItemDecoration)
    }
}
