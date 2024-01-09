package com.lit.remindme

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lit.remindme.Notifications.NotificationWorkerStarter
import com.lit.remindme.feature_events.domain.model.RemindMeConstants
import com.lit.remindme.feature_events.presentation.add_edit_event.AddEditEventScreen
import com.lit.remindme.feature_events.presentation.events.EventsScreen
import com.lit.remindme.feature_events.presentation.util.PermissionsCheck
import com.lit.remindme.feature_events.presentation.util.Screen
import com.lit.remindme.ui.theme.RemindMeTheme
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val _notificationHasStarted = mutableStateOf<Int>(0)
    private val notificationHasStarted: State<Int> = _notificationHasStarted

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.d("DBG Main","#00")
        setContent {
//            Log.d("DBG Main","#01")
            requestGeneralPermissions()

            val eventId = intent.getIntExtra("eventId",-1)

            RemindMeTheme {
                if(notificationHasStarted.value == 0) {
                    _notificationHasStarted.value = 1
                    NotificationWorkerStarter(applicationContext).start()
                }

                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.EventsScreen.route
                    ) {
                        composable(route = Screen.EventsScreen.route) {
                            EventsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditEventScreen.route +
                                    "?eventId={eventId}",
                            arguments = listOf(
                                navArgument(
                                    name = "eventId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditEventScreen(
                                navController = navController
                            )
                        }
                    }
                    if(eventId > -1)
                        navController.navigate(Screen.AddEditEventScreen.route + "?eventId=$eventId")
                }
            }
        }
    }

    @Composable
    private fun requestGeneralPermissions() {
        val localContext = LocalContext.current

        val permissionsToCheckFor = mutableListOf<String>()
        if (!PermissionsCheck().hasContactsPermission(localContext))
            permissionsToCheckFor.add(Manifest.permission.READ_CONTACTS)

        if(!PermissionsCheck().hasPostPermission(localContext)) {
            permissionsToCheckFor.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionsToCheckFor.isNotEmpty()) {
            ActivityCompat.requestPermissions(this,permissionsToCheckFor.toTypedArray(),
                RemindMeConstants.REQUEST_PERMISSIONS_REQUEST_CODE)
        }

        if (!PermissionsCheck().hasExactAlarmPermission(localContext)){
            val onClickFunc = {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }

            informationDialog(this.getString(R.string.string_permission_request_title), this.getString(R.string.string_permission_request_exact_notifications), this.getString(R.string.string_permission_request_button_continue), onClickFunc)
        }
    }

    @Composable
    private fun informationDialog(title : String, infoText : String, button : String, onClickFunc : () -> Unit) {
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(title) },
                text = { Text(infoText) },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false
                            onClickFunc()
                        }
                    ) {
                        Text(button)
                    }
                }
            )
        }
    }
}