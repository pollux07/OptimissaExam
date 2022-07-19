package daniel.leon.optimissaexam.usecases.addcontact

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import daniel.leon.optimissaexam.model.domain.ContactsData
import daniel.leon.optimissaexam.navigate.AppScreens
import daniel.leon.optimissaexam.provider.preferences.AdminSQLiteOpenHelper

private var globalId: String = ""
private var globaPhone: String = ""
private var globalName: String = ""

@Composable
fun AddContact(context: Context, navController: NavController){
    Scaffold(topBar = {
        TopAppBar() {
            Text("Agregar Contacto")
        }
    }) {
        MainComponents(context, navController)
    }
}

@Composable
fun MainComponents(context: Context,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Optimissa",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 100.dp),
        )
        Text("Ingresa tus datos",
            fontSize = 20.sp,)
        TextInputs()
        MainButtons(context, navController)
    }
}

@Composable
fun TextInputs() {
    Column() {
        Spacer(modifier = Modifier.height(16.dp))
        IdInputText()
        Spacer(modifier = Modifier.height(16.dp))
        CellphoneInputText()
        Spacer(modifier = Modifier.height(16.dp))
        NameInpitText()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IdInputText() {

    var text by remember { mutableStateOf(globalId) }

    TextField(
        value = text,
        onValueChange = {text = it},
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Ingresa un ID")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    globalId = text
}

@Composable
fun CellphoneInputText() {
    var text by remember { mutableStateOf(globaPhone) }

    TextField(
        value = text,
        onValueChange = {text = it},
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Ingresa tu teléfono celular")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
    globaPhone = text
}

@Composable
fun NameInpitText(){
    var text by remember { mutableStateOf(globalName) }

    TextField(
        value = text,
        onValueChange = {text = it},
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Ingresa tu nombre completo")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    globalName = text
}

@Composable
fun MainButtons(context: Context, navController: NavController) {
    Row() {
        AddBtn(context)
        ShowContactsBtn(navController)
    }
}

@Composable
fun AddBtn(context: Context,) {
    Button(onClick = {
        val sqlAdmin = AdminSQLiteOpenHelper(context, "contactos", null, 1)
        val bd = sqlAdmin.writableDatabase
        val registro = ContentValues()
        registro.put("id", globalId)
        registro.put("phone", globaPhone)
        registro.put("username", globalName)
        bd.insert("contactos", null, registro)



        /*val fila = bd.rawQuery("select id,phone, username from contactos", null)
        if (fila.moveToFirst()) {
            var contactsList: MutableList<ContactsData> = mutableListOf()
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
        }*/
        bd.close()

        globalId = ""
        globaPhone = ""
        globalName = ""
        }) {
        Text("Agregar")
    }
}

@Composable
fun ShowContactsBtn(navController: NavController) {
    Button(modifier = Modifier
        .padding(start = 115.dp),
        onClick = {
        navController.navigate(route = AppScreens.ShowContacts.route)
    }) {
        Text("Ver Registros")
    }
}
