package ch.b.retrofitandcoroutines.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import ch.b.retrofitandcoroutines.R
import ch.b.retrofitandcoroutines.data.api.RetrofitBuilder
import ch.b.retrofitandcoroutines.data.model.UserDTO
import ch.b.retrofitandcoroutines.data.repository.MainDataSource
import ch.b.retrofitandcoroutines.databinding.FragmentUserBinding
import ch.b.retrofitandcoroutines.ui.base.ViewModelFactory
import ch.b.retrofitandcoroutines.ui.main.adapter.AdapterOnClick
import ch.b.retrofitandcoroutines.ui.main.adapter.MainAdapter
import ch.b.retrofitandcoroutines.ui.main.viewmodel.MainViewModel

class MainFragment : Fragment(), AdapterOnClick {


    private val viewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(MainDataSource(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }
    private lateinit var adapter: MainAdapter
    private lateinit var binding: FragmentUserBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view)
        viewModel.getUsers()
        setupObservers()
        setupUI()

    }

   private  fun setupObservers() {
        viewModel.newResponse.observe(viewLifecycleOwner, Observer {resources ->
            binding.recyclerView.visibility = View.VISIBLE
            adapter.isShimmer = false
            retrieveList(resources.data!!)
        })
    }

    private fun retrieveList(usersDTO: List<UserDTO>) {
        adapter.apply {
            addUsers(usersDTO)
        }
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = GridLayoutManager(context,2)
        adapter = MainAdapter(arrayListOf(), this)
        binding.recyclerView.adapter = adapter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onClick(item: UserDTO) {
        val argumentOne = bundleOf(DetailInfoFragment.KEY to item.downloadedPicture)
        findNavController().navigate(R.id.action_mainFragment_to_detailInfo, argumentOne)
    }

}
