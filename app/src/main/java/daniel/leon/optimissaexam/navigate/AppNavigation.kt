package daniel.leon.optimissaexam.navigate

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import daniel.leon.optimissaexam.usecases.addcontact.AddContact
import daniel.leon.optimissaexam.usecases.showcontacts.ShowContacts

@Composable
fun AppNavigation(context: Context) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.AddContact.route) {
        composable(route = AppScreens.AddContact.route) {
            AddContact(context, navController)
        }
        composable(route = AppScreens.ShowContacts.route) {
            ShowContacts(context)
        }
    }
}