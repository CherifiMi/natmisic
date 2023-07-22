package com.example.natmisic.feature.presentation.folder_picker.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.natmisic.R
import com.example.natmisic.feature.presentation.folder_picker.FolderPickerViewModel
import com.example.natmisic.feature.presentation.util.Screens

@Composable
fun PageIII(
    navController: NavHostController,
    viewModel: FolderPickerViewModel
) {
    // open folder picker and save root path
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also { uri ->
            viewModel.saveRootFolderName(uri.dataString.toString())
            navController.navigate(Screens.HOME.name)
        }
    }


    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.size(80.dp))
        Image(
            painter = painterResource(id = R.drawable.page3),
            contentDescription = "",
            modifier = Modifier
                .height(256.dp)
                .aspectRatio(1f)
        )
        Text(
            text = stringResource(id = R.string.page3_title),
            color = MaterialTheme.colors.secondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 100.dp)
        )
        /*Text(
            text = stringResource(id = R.string.lorem),
            color = MaterialTheme.colors.secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 70.dp).padding(bottom = 48.dp),
            maxLines = 2
        )*/

        Button(onClick = { activityResultLauncher.launch(intent) }, shape = RoundedCornerShape(40), border = BorderStroke(2.dp, MaterialTheme.colors.secondary))
        {
            Text(modifier = Modifier.padding(vertical = 8.dp, horizontal = 80.dp), text = "Select", color = MaterialTheme.colors.secondary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}