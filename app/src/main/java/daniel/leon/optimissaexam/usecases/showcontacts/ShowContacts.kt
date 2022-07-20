package daniel.leon.optimissaexam.usecases.showcontacts

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import daniel.leon.optimissaexam.model.domain.ContactsData
import daniel.leon.optimissaexam.provider.preferences.AdminSQLiteOpenHelper

@Composable
fun ShowContacts(context: Context, navController: NavController) {
    Scaffold(topBar = {
        TopAppBar() {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Back",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mostrar Contactos")
        }
    }) {
        MainComponents(context)
    }
}

@Composable
fun MainComponents(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchInputText(context)
        Spacer(modifier = Modifier.height(16.dp))
        ShowContactList("", context, false)
    }
}

@Composable
fun SearchInputText(context: Context) {
    var text by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    TextField(
        value = text,
        onValueChange = {text = it},
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar contacto")},
        trailingIcon = {
            Icon(imageVector = Icons.Default.Search,
                "search",
                modifier = Modifier.clickable {
                    isSearching = true
                }
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    if (isSearching) {
        ShowContactList(text, context, isSearching)
    }
}

@Composable
fun ShowContactList(searchId: String?, context: Context, isSearching: Boolean) {
    val sqlAdmin = AdminSQLiteOpenHelper(context, "contactos", null, 1)
    val bd = sqlAdmin.writableDatabase
    var fila = bd.rawQuery("select * from contactos", null)
    /*if (searchId != "" && isSearching) {
        fila = bd.rawQuery("select * from contactos where id=${searchId}", null)
    }*/

    var contactsList: MutableList<ContactsData>

    val listContact = arrayListOf<MutableList<ContactsData>>()

    if (fila.moveToFirst()) {
        do {
            contactsList =  mutableListOf(
                ContactsData(fila.getString(0), fila.getString(1), fila.getString(2))
            )
            listContact.add(contactsList)
        } while (fila.moveToNext())

        LazyContacts(listContact)

    } else {
        Log.e("Error", "no se pudo almacenar")
    }
}


@Composable
fun LazyContacts(listContact: ArrayList<MutableList<ContactsData>>) {
    for (item  in listContact) {
        LazyColumn {
            items(item) { contact ->
                ContactInfo(contact)
            }
        }
    }
}

@Composable
fun ContactInfo(contact: ContactsData) {
    Column() {
        MyText("ID: ${contact.idUser}")
        MyText("Número: ${contact.phone}")
        MyText("Nombre: ${contact.userName}")
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MyText(text: String) {
    Text(text)
}
