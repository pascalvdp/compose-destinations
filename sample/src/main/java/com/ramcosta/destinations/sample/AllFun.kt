package com.example.boodschappenlijst

import android.content.Context
import android.widget.Toast

//@Composable
fun toast(ctx: Context, obj: Any) {  //val ctx = LocalContext.current => dit bovenaan in de composable zetten
    Toast.makeText(ctx, obj.toString(), Toast.LENGTH_SHORT).show()
}