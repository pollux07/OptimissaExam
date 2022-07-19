package daniel.leon.optimissaexam.navigate

sealed class AppScreens (val route: String) {
    object AddContact: AppScreens("add_contact")
    object ShowContacts: AppScreens("show_contacts")
}
