package otus.homework.coroutines

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import otus.homework.coroutines.databinding.FragmentCatBinding
import javax.inject.Inject

class CatFragmentMVVM : Fragment(R.layout.fragment_cat) {


    @Inject
    lateinit var viewModelFactory: CatViewModel.Factory

    private val viewModel: CatViewModel by assistedViewModels { viewModelFactory.create() }

    private val layoutBinding: FragmentCatBinding by viewBinding(
        FragmentCatBinding::bind
    )

    lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

        viewModel.catData.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> {
                    populate(result.data.fact, result.data.imageUrl)
                    hideLoading()
                }
                is Result.Error -> {
                    hideLoading()
                    showToast(result.exception.message.toString())
                }
            }
        }
    }

    private fun setListeners() = with(layoutBinding) {
        buttonRefresh.setOnClickListener {
            viewModel.loadData()
        }
        buttonCancel.setOnClickListener {
            hideLoading()
            viewModel.cancelLoad()
        }
    }

    private fun populate(factText: String, imageResourceUrl: String) = with(layoutBinding) {
        factTextView.text = factText
        Picasso.get().load(imageResourceUrl).into(factImg)
    }

    private fun showLoading() {
        progressBar.toVisible()
    }

    private fun hideLoading() {
        progressBar.toInvisible()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}