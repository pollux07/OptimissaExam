package daniel.leon.optimissaexam.usecases.showcontacts

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import daniel.leon.optimissaexam.model.domain.ContactsData
import daniel.leon.optimissaexam.provider.preferences.AdminSQLiteOpenHelper
import daniel.leon.optimissaexam.usecases.addcontact.MainComponents
import kotlin.contracts.contract

@Composable
fun ShowContacts(context: Context) {
    Scaffold(topBar = {
        TopAppBar() {
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
        SearchInputText()
        ContactList(context)
    }
}

@Composable
fun SearchInputText() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {text = it},
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar contacto")},
        trailingIcon = {
            Icon(imageVector = Icons.Default.Search, "search")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun ContactList(context: Context) {
    val sqlAdmin = AdminSQLiteOpenHelper(context, "contactos", null, 1)
    val bd = sqlAdmin.writableDatabase
    val fila = bd.rawQuery("select id,phone, username from contactos", null)
    var contactsList: MutableList<ContactsData> = mutableListOf()
    if (fila.moveToFirst()) {
        do {
            contactsList =  mutableListOf(
                ContactsData(fila.getString(0), fila.getString(1), fila.getString(2))
            )
        } while (fila.moveToNext())

        for (contact  in contactsList) {
            Log.i("info recibida:", "${contact.idUser}, ${contact.phone}, ${contact.userName}")
        }
        //Log.i("info recibida:", "${contact.idUser}, ${fila.getString(1)}, ${fila.getString(2)}")
    } else {
        Log.w("Error", "no se pudo almacenar")
    }


    LazyColumn {
        items(contactsList) { contact ->
            ContactInfo(contact)
        }
    }
}

@Composable
fun ContactInfo(contact: ContactsData) {
    Row() {
        MyText(contact.idUser)
        MyText(contact.phone)
        MyText(contact.userName)
    }
}

@Composable
fun MyText(text: String) {
    Text(text)
}
