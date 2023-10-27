package com.isaiajereb.gymandgram;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.isaiajereb.gymandgram.ui.ConfigurarPerfilActivity;

import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestConfigurarPerfilActivity {


    @Rule
    public ActivityTestRule<ConfigurarPerfilActivity> activityRule = new ActivityTestRule<>(ConfigurarPerfilActivity.class,false,false);

    @Before
    public void setUp(){
        Intent intent = new Intent();
        intent.putExtra("fotoPerfil", "fotoperfil.png");
        intent.putExtra("usuarioNombre", "");
        intent.putExtra("usuarioMail", "");
        intent.putExtra("usuarioGenero", "Masculino");
        intent.putExtra("usuarioEdad", 0);

        activityRule.launchActivity(intent);
    }

    @Test
    public void guardarSinNombre(){
        /*
            Situacion:
                Intenta guardar el perfil sin haber indicado nombre
            Resultado:
                No se debe abrir el dialog para guardar cambios
         */
        //Borrar edad inicial (0)
        onView(withId(R.id.edad_ET)).perform(replaceText(""));

        //Escribir correo
        onView(withId(R.id.email_ET)).perform(typeText("testemail@gmail.com"));
        Espresso.closeSoftKeyboard();

        //Escribir edad
        onView(withId(R.id.edad_ET)).perform(replaceText(""));
        onView(withId(R.id.edad_ET)).perform(typeText("99"));
        Espresso.closeSoftKeyboard();

        //Clickear guardar
        onView(withId(R.id.guardar_perfil_boton)).perform(click());

        //Verificar que no se haya abierto el dialog
        onView(withText("ACEPTAR")).check(doesNotExist());
    }

    @Test
    public void guardarSinCorreo(){
        /*
            Situacion:
                Intenta guardar el perfil sin haber indicado correo
            Resultado:
                No se debe abrir el dialog para guardar cambios
         */
        //Borrar edad inicial (0)
        onView(withId(R.id.edad_ET)).perform(replaceText(""));

        //Escribir nombre
        onView(withId(R.id.nombre_ET)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();

        //Escribir edad
        onView(withId(R.id.edad_ET)).perform(typeText("99"));
        Espresso.closeSoftKeyboard();

        //Clickear guardar
        onView(withId(R.id.guardar_perfil_boton)).perform(click());

        //Verificar que no se haya abierto el dialog
        onView(withText("ACEPTAR")).check(doesNotExist());
    }

    @Test
    public void guardarSinEdad(){
        /*
            Situacion:
                Intenta guardar el perfil sin haber indicado edad
            Resultado:
                No se debe abrir el dialog para guardar cambios
         */

        //Borrar edad inicial (0)
        onView(withId(R.id.edad_ET)).perform(replaceText(""));

        //Escribir nombre
        onView(withId(R.id.nombre_ET)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();

        //Escribir correo
        onView(withId(R.id.email_ET)).perform(typeText("testemail@gmail.com"));
        Espresso.closeSoftKeyboard();

        //Clickear guardar
        onView(withId(R.id.guardar_perfil_boton)).perform(click());

        //Verificar que no se haya abierto el dialog
        onView(withText("ACEPTAR")).check(doesNotExist());
    }

    @Test
    public void guardarSinNada(){
        /*
            Situacion:
                Intenta guardar el perfil sin haber indicado nombre, correo ni edad
            Resultado:
                No se debe abrir el dialog para guardar cambios
         */

        //Borrar edad inicial (0)
        onView(withId(R.id.edad_ET)).perform(replaceText(""));

        //Clickear guardar
        onView(withId(R.id.guardar_perfil_boton)).perform(click());

        //Verificar que no se haya abierto el dialog
        onView(withText("ACEPTAR")).check(doesNotExist());
    }

    @Test
    public void guardarCorrecto(){
        /*
            Situacion:
                Intenta guardar el perfil completando todos los campos
            Resultado:
                Se abre el dialog correctamente
         */

        //Borrar edad inicial (0)
        onView(withId(R.id.edad_ET)).perform(replaceText(""));

        //Escribir nombre
        onView(withId(R.id.nombre_ET)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();

        //Escribir correo
        onView(withId(R.id.email_ET)).perform(typeText("testemail@gmail.com"));
        Espresso.closeSoftKeyboard();

        //Escribir edad
        onView(withId(R.id.edad_ET)).perform(typeText("99"));
        Espresso.closeSoftKeyboard();

        //Clickear guardar
        onView(withId(R.id.guardar_perfil_boton)).perform(click());

        //Verificar que se haya abierto el dialog
        onView(withText("ACEPTAR")).check(matches(isDisplayed()));

    }
}
