package com.example.namma_mela.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namma_mela.R
import com.example.namma_mela.data.AppDatabase
import com.example.namma_mela.data.MelaRepository
import com.example.namma_mela.databinding.FragmentFanwallBinding
import com.example.namma_mela.ui.MelaViewModel
import com.example.namma_mela.ui.adapters.CommentAdapter

class FanWallFragment : Fragment(R.layout.fragment_fanwall) {
    
    private val viewModel: MelaViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = MelaRepository(database.seatDao())
        MelaViewModel.Factory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFanwallBinding.bind(view)

        val adapter = CommentAdapter()
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewComments.adapter = adapter

        viewModel.comments.observe(viewLifecycleOwner) { commentList ->
            adapter.submitList(commentList)
        }

        binding.btnPostComment.setOnClickListener {
            val author = binding.etAuthorName.text.toString().trim()
            val text = binding.etCommentText.text.toString().trim()

            if (author.isNotEmpty() && text.isNotEmpty()) {
                viewModel.postComment(author, text)
                binding.etAuthorName.text?.clear()
                binding.etCommentText.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter name and comment", Toast.LENGTH_SHORT).show()
            }
        }
    }
}