package com.isaiajereb.gymandgram;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.isaiajereb.gymandgram.ui.EditarRutinaFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditarRutinaFragmentTest {
    private FragmentScenario<EditarRutinaFragment> scenario;

    @Before
    public void setUp(){
        scenario = FragmentScenario.launchInContainer(EditarRutinaFragment.class);
    }

    @Test
    public void cambiarNombre_sinNombre(){
        /*
            Situacion:
                El usuario intenta cambiar el nombre de la rutina, pero no ingresa ningun nombre
            Resultado:
                No se debe poder confirmar el cambio y el dialog permanece abierto
         */
        String nombreVacio = "";

        //Abrir dialog
        onView(withId(R.id.nombreRutinaButton)).perform(click());

        //Confirmar que esta abierto
        onView(withText("Ingrese un nuevo nombre para la rutina:")).check(matches(isDisplayed()));

        //Escribir nombre vacio
        onView(withId(R.id.et_nuevo_nombre)).perform(typeTextIntoFocusedView(nombreVacio));
        Espresso.closeSoftKeyboard();

        //Intentar confirmar
        onView(withId(R.id.boton_confirmar)).perform(click());

        //Verificar que el dialog sigue abierto
        onView(withText("Ingrese un nuevo nombre para la rutina:")).check(matches(isDisplayed()));
    }

    @Test
    public void cambiarNombre_confirmar(){
        /*
            Situacion:
                El usuario intenta cambiar el nombre de la rutina, ingresa uno y confirma
            Resultado:
                Se cierra el dialog y se cambia el nombre mostrado en pantalla
         */

        String nombre = "Nombre para test";

        //Abrir dialog
        onView(withId(R.id.nombreRutinaButton)).perform(click());

        //Confirmar que esta abierto
        onView(withText("Ingrese un nuevo nombre para la rutina:")).check(matches(isDisplayed()));

        //Escribir nombre
        onView(withId(R.id.et_nuevo_nombre)).perform(typeTextIntoFocusedView(nombre));
        Espresso.closeSoftKeyboard();

        //Confirmar
        onView(withId(R.id.boton_confirmar)).perform(click());

        //Verificar que se cerro el dialog
        onView(withText("Ingrese un nuevo nombre para la rutina:")).check(doesNotExist());

        //Verificar que se cambio el nombre
        onView(withId(R.id.nombreRutinaButton)).check(matches(withText(nombre)));
    }
}
