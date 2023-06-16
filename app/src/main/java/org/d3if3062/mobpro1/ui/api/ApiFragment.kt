package org.d3if3062.mobpro1.ui.api

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if3062.mobpro1.MainActivity
import org.d3if3062.mobpro1.databinding.FragmentApiBinding
import org.d3if3062.mobpro1.netwrok.KegiatanApi

class ApiFragment : Fragment() {
    private val viewModel: ApiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private lateinit var binding: FragmentApiBinding
    private lateinit var myAdapter: ApiAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentApiBinding.inflate(layoutInflater, container, false)
        myAdapter = ApiAdapter()

        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(context,
                RecyclerView.VERTICAL)
            )
            adapter = myAdapter

            setHasFixedSize(true)
        }
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }
        viewModel.getStatus().observe(viewLifecycleOwner){
            updateProgress(it)
        }
        viewModel.scheduleUpdater(requireActivity().application)
    }

    private fun updateProgress(status:KegiatanApi.ApiStatus){
        when (status) {
            KegiatanApi.ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            KegiatanApi.ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
            }
            KegiatanApi.ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }
}
