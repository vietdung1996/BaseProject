package com.example.baseproject.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.baseproject.databinding.FragmentUserListBinding
import com.example.baseproject.ui.UserViewModel
import com.example.baseproject.ui.UsersState
import com.example.baseproject.ui.adapter.UserAdapter
import com.example.core.base.ViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for displaying the list of users
 */
@AndroidEntryPoint
class UserListFragment : ViewModelFragment<FragmentUserListBinding, UserViewModel>() {

    override val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setupRecyclerView() đã được chuyển vào initView()
    }

    override fun initView() {
        adapter = UserAdapter { user ->
            val action = UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(user.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewUsers.adapter = adapter
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersState.collectLatest { state ->
                    when (state) {
                        is UsersState.Loading -> handleLoading(true)
                        is UsersState.Success -> showUsers(state)
                        is UsersState.Empty -> showEmpty()
                        is UsersState.Error -> handleError(state.message)
                    }
                }
            }
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerViewUsers.visibility = View.GONE
            binding.textViewEmpty.visibility = View.GONE
            binding.textViewError.visibility = View.GONE
        }
    }

    override fun handleError(message: String) {
        super.handleError(message)
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.GONE
        binding.textViewEmpty.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message
    }

    private fun showUsers(state: UsersState.Success) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.VISIBLE
        binding.textViewEmpty.visibility = View.GONE
        binding.textViewError.visibility = View.GONE
        adapter.submitList(state.users)
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.GONE
        binding.textViewEmpty.visibility = View.VISIBLE
        binding.textViewError.visibility = View.GONE
    }
} 