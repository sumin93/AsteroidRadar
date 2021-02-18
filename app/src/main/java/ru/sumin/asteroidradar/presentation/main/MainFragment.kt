package ru.sumin.asteroidradar.presentation.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.sumin.asteroidradar.R
import ru.sumin.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.error_retry_button) { _, _ ->
                viewModel.getAsteroids()
            }
            .setOnDismissListener {
                viewModel.errorProcessed()
            }
            .create()

        val adapter = AsteroidAdapter {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        }

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            binding.pictureOfDay = it
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                alertDialog.show()
            }
        })

        viewModel.getAsteroids()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.showWeekAsteroids()
            R.id.show_today_menu -> viewModel.showTodayAsteroids()
            R.id.show_saved_menu -> viewModel.showSavedAsteroids()
        }
        return true
    }
}
