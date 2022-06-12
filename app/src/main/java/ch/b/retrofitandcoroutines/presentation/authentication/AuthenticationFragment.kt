package ch.b.retrofitandcoroutines.presentation.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ch.b.retrofitandcoroutines.R
import ch.b.retrofitandcoroutines.core.BaseSingleRegistrationStringMapper
import ch.b.retrofitandcoroutines.databinding.FragmentAuthorizationBinding
import ch.b.retrofitandcoroutines.presentation.all_posts.screen.PhotographersFragment
import ch.b.retrofitandcoroutines.presentation.container_screens.FragmentScreen
import ch.b.retrofitandcoroutines.presentation.core.BaseFragment
import ch.b.retrofitandcoroutines.presentation.navigate.RouterProvider
import ch.b.retrofitandcoroutines.presentation.registration.RegistrationFragment



class AuthenticationFragment :
    BaseFragment<FragmentAuthorizationBinding>(FragmentAuthorizationBinding::inflate) {
    private val viewModel: AuthenticationViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registration.setOnClickListener {
            val fragment = RegistrationFragment()
            val nextScreen = FragmentScreen(fragment.newInstance())
            (parentFragment as RouterProvider).router.navigateTo(nextScreen)
        }
        binding.authentication.setOnClickListener {
            try {
                lifecycleScope.launchWhenCreated {
                    viewModel.observer(this@AuthenticationFragment, { list ->
                        list.map {
                            it.map(object : BaseSingleRegistrationStringMapper.SingleStringMapper {
                                override fun map(
                                    accessToken: String,
                                    refreshToken: String,
                                    successRegister: Boolean
                                ) {
                                    if (accessToken.isNotEmpty()) {
                                        val fragment = PhotographersFragment()
                                        val nextScreen = FragmentScreen(fragment.newInstance())
                                        (parentFragment as RouterProvider).router.navigateTo(
                                            nextScreen
                                        )
                                    }
                                }

                                override fun map(message: String) {
                                    binding.state.text = message
                                }

                                override fun map(progress: Boolean) {
                                    binding.state.text = activity!!.getString(R.string.please_wait)
                                }

                            })
                        }

                    })
                }
                val phone = binding.numberOfPhone.text.toString().toLong()
                val password = binding.password.text.toString()
                viewModel.authentication(phone, password)
            } catch (e: Exception) {
                binding.state.text = "ошибка"
            }
        }

    }

    fun newInstance(): AuthenticationFragment {
        return AuthenticationFragment()
    }

}