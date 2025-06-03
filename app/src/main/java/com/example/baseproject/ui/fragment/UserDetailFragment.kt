package com.example.baseproject.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentUserDetailBinding
import com.example.baseproject.ui.viewmodel.UserDetailState
import com.example.baseproject.ui.viewmodel.UserDetailViewModel
import com.example.core.base.ViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for displaying user details
 */
@AndroidEntryPoint
class UserDetailFragment : ViewModelFragment<FragmentUserDetailBinding, UserDetailViewModel>() {

    private val args: UserDetailFragmentArgs by navArgs()
    override val viewModel: UserDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserById(args.userId)
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collectLatest { state ->
                    when (state) {
                        is UserDetailState.Loading -> showLoading()
                        is UserDetailState.Success -> showUserDetails(state)
                        is UserDetailState.Error -> handleError(state.message)
                    }
                }
            }
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.textViewError.visibility = View.GONE
        }
    }

    override fun handleError(message: String) {
        super.handleError(message)
        binding.progressBar.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewError.visibility = View.GONE
    }

    private fun showUserDetails(state: UserDetailState.Success) {
        binding.progressBar.visibility = View.GONE
        binding.textViewError.visibility = View.GONE

        val user = state.user
        binding.textViewName.text = user.name
        binding.textViewEmail.text = user.email
        binding.textViewId.text = "ID: ${user.id}"

//        user.avatarUrl?.let { url ->
//            binding.imageViewAvatar.load(url) {
//                crossfade(true)
//                placeholder(R.drawable.ic_person)
//                error(R.drawable.ic_person)
//                transformations(CircleCropTransformation())
//            }
//        } ?: binding.imageViewAvatar.load(R.drawable.ic_person)
    }
} 