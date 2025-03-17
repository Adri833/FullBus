package es.thatapps.fullbus.presentation.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import es.thatapps.fullbus.R
import es.thatapps.fullbus.constants.FullBusConstants
import es.thatapps.fullbus.presentation.components.GoogleSignInButton
import es.thatapps.fullbus.presentation.components.PasswordTextField
import es.thatapps.fullbus.presentation.components.adjustForMobile
import es.thatapps.fullbus.presentation.loading.LoadingScreen
import es.thatapps.fullbus.utils.AsyncResult

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigationToRegister: () -> Unit,
    navigationToHome: () -> Unit
) {

    // Init
    LaunchedEffect(Unit) {
        viewModel.resetAsyncResult()
        if (viewModel.isUserLoggedIn()) { navigationToHome() }
    }

    // UI
    LoginView(viewModel, navigationToRegister)

    // Login observer
    val asyncResult by viewModel.authState.collectAsState()
    val passwordResetState by viewModel.passwordResetState.collectAsState()
    val context = LocalContext.current

    // Login
    when (asyncResult) {
        is AsyncResult.Loading -> LoadingScreen()
        is AsyncResult.Success -> {
            viewModel.resetAsyncResult() // Reestablece el estado para evitar un bucle
            Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
            navigationToHome()
        }
        else -> {}
    }

    // Password observer
    when (passwordResetState) {
        is AsyncResult.Loading -> LoadingScreen()
        is AsyncResult.Success -> {
            viewModel.resetAsyncResult() // Reestablece el estado para evitar un bucle
            Toast.makeText(context, "Comprueba tu bandeja de entrada", Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
}

@Composable
private fun LoginView(
    viewModel: LoginViewModel,
    navigationToRegister: () -> Unit,
) {
    // Estados para almacenar los valores de entrada
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val asyncResult by viewModel.authState.collectAsState()

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }

    // Interfaz de google
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        viewModel.googleSignInObserver(result, oneTapClient)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .adjustForMobile()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_fullbus),
                contentDescription = "Logo de la aplicacion",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(
                text = "Iniciar sesión",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )

            // Campo de texto para el email
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(0xFFfe1200),
                    focusedIndicatorColor = Color(0xFFfe1200),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de texto para la contraseña
            PasswordTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                }
            )

            TextButton(
                onClick = { viewModel.resetPassword(email.value) }, // Llama a la función de restablecimiento de contraseña
                modifier = Modifier
                    .align(Alignment.End),
            ) {
                Text("¿Olvidaste tu contraseña?", fontSize = 13.sp, color = Color(0xFFfe1200))
            }

            // Mostrar mensaje de error en un recuadro rojo si hay algún error
            if (asyncResult is AsyncResult.Error) {
                val errorMessage = when (val msg = (asyncResult as AsyncResult.Error).message) {
                    is Int -> context.getString(msg)
                    is String -> msg
                    else -> "Error desconocido"
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(
                            Color(0xFFFFD2D7),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center // Centrar el texto
                ) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    // Oculta el teclado
                    keyboardController?.hide()
                    viewModel.login(email.value, password.value) // Llama a la función de login
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Iniciar Sesión", fontSize = 17.sp, color = MaterialTheme.colorScheme.background)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de inicio de sesión con Google
            GoogleSignInButton {
                launchGoogleSignIn(
                    context = context,
                    viewModel = viewModel,
                    launcher = googleLauncher
                )
            }
        }

        // Enlace a la pantalla de registro
        TextButton(
            onClick = { navigationToRegister() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text(
                buildAnnotatedString {
                    append("¿No tienes cuenta? ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Regístrate")
                    }
                },
                color = Color(0xFFfe1200)
            )
        }
    }
}

private fun launchGoogleSignIn(
    context: Context,
    viewModel: LoginViewModel,
    launcher: ActivityResultLauncher<IntentSenderRequest>,
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(FullBusConstants.GOOGLE_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
    viewModel.setLoadingState()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            viewModel.resetAsyncResult()
            launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
        }

        .addOnFailureListener { e ->
            Toast.makeText(context, "Error al iniciar sesion", Toast.LENGTH_SHORT).show()
            Log.e("ERROR", e.message.toString() + " " + e.localizedMessage)
            viewModel.resetAsyncResult()
        }
}