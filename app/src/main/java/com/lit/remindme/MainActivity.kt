package com.lit.remindme

import android.Manifest
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


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val _notificationHasStarted = mutableStateOf<Int>(0)
    private val notificationHasStarted: State<Int> = _notificationHasStarted

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        setContent {
            val eventId = intent.getIntExtra("eventId",-1)

//            Log.d("DBG Main","Start")
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

    private fun requestPermissions() {
        val permissionsToCheckFor = mutableListOf<String>()
        if (!PermissionsCheck().hasContactsPermission(this))
            permissionsToCheckFor.add(Manifest.permission.READ_CONTACTS)

        if (permissionsToCheckFor.isNotEmpty()) {
            ActivityCompat.requestPermissions(this,permissionsToCheckFor.toTypedArray(),
                RemindMeConstants.REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }
}
