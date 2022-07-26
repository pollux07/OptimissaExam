package daniel.leon.optimissaexam.usecases.showcontacts

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import daniel.leon.optimissaexam.R
import daniel.leon.optimissaexam.model.domain.ContactsData
import daniel.leon.optimissaexam.provider.preferences.AdminSQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

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
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        SearchView(textState)
        Spacer(modifier = Modifier.height(16.dp))
        ShowContactList( context, state = textState)
    }
}



@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.purple_500),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ShowContactList(
    context: Context,
    state: MutableState<TextFieldValue>
) {
    val contacst =  getContactsList(context)
    var filteredContacts: ArrayList<ContactsData>

    LazyColumn {
        val searchedText = state.value.text
        filteredContacts = if (searchedText.isEmpty()) {
            contacst
            } else {
                val resultList = ArrayList<ContactsData> ()
                for (contact in contacst) {
                    if (contact.idUser.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(contact)
                    }
                }
                resultList
            }
            items(filteredContacts) { contact ->
                ContactInfo(contact)
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

fun getContactsList(context: Context): ArrayList<ContactsData> {
    val contactsArray =ArrayList<ContactsData>()
    val sqlAdmin = AdminSQLiteOpenHelper(context, "contactos", null, 1)
    val bd = sqlAdmin.writableDatabase
    val fila = bd.rawQuery("select * from contactos", null)
    if (contactsArray.isEmpty()) {
        if (fila.moveToFirst()) {
            do {
                val contact = ContactsData(fila.getString(0), fila.getString(1), fila.getString(2))
                contactsArray.add(contact)
            } while (fila.moveToNext())
        } else {
            Log.e("Error", "no se pudo almacenar")
        }
    }
    return contactsArray
}
