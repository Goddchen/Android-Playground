package de.goddchen.android.playground.playgrounds.biometrics

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import de.goddchen.android.playground.R
import kotlinx.android.synthetic.main.fragment_playground_biometrics.*
import java.util.concurrent.Executor

@Suppress("unused")
class BiometricsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_playground_biometrics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        secret.transformationMethod = PasswordTransformationMethod()
        unlock.setOnClickListener {
            activity?.run {
                BiometricPrompt(this, Executor {
                    runOnUiThread(it)
                }, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Snackbar.make(view, "Error: $errString", Snackbar.LENGTH_SHORT).show()
                        secret.transformationMethod = PasswordTransformationMethod()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        secret.transformationMethod = null
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Snackbar.make(view, "Failed", Snackbar.LENGTH_SHORT).show()
                        secret.transformationMethod = PasswordTransformationMethod()
                    }
                }).authenticate(
                    BiometricPrompt.PromptInfo.Builder()
                        .setDescription("Description")
                        .setTitle("Title")
                        .setSubtitle("Subtitle")
                        .setNegativeButtonText("Cancel")
                        .build()
                )
            }
        }
    }
}