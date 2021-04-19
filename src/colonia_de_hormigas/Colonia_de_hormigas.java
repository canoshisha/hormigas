/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colonia_de_hormigas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 * @author Alebuntu
 */
public class Colonia_de_hormigas {

    // Equivale a tau_0
    private static final double C = 1.0;

    //Parametro para calcular la probabilidad de aceptacion de la hormiga
    private static final double ALPHA = 1;
    private static final double BETA = 0.5;

    // Equivale a la rho de la teoría (Lo que me va reduciendo el rastro a lo largo del tiempo), se va eliminando el rastro
    private static final double EVAPORACION = 0.5;

    //cantidad de rastro
    private static final double Q = 500;

    //factor para carcular numero de hormigas
    private static final double FACTOR = 0.3;

    //numero de iteraciones
    private static final int ITERACIONES = 100;

    //numero de elementos y de hormigas
    private int numElementos;
    private int numHormigas;

    //Cantidad de feromonas depositadas en cada elementos
    private double[] rastros;

    // Lista con las hormigas que forman la colonia
    private List<Hormiga> hormigas = new ArrayList<>();

    // Probabilidades de usar un elemento
    private double[] probTransicion;

    //mejor coste y recorrido
    private int[] mejorRecorrido;
    private double mejorCoste = 0;

    //capacidad de la mochila
    private static final double Capacidad = 3000.0;

    //array de pesos y beneficios
    private double[] pesos;
    private double[] beneficios;

    //duble con el mejor peso
    private double mejorPeso = 0;

    /**
     * constructor del hormiguero
     */
    public Colonia_de_hormigas(int num_elemen, double[] pesos, double[] beneficios) {
        numElementos = num_elemen;
        numHormigas = (int) (this.numElementos * FACTOR);
        rastros = new double[this.numElementos];
        probTransicion = new double[this.numElementos];
        this.pesos = pesos;
        this.beneficios = beneficios;
        IntStream.range(0, numHormigas).forEach(i -> hormigas.add(new Hormiga(this.numElementos)));//de 0 al numero de hormigas  hace una hormiga
    }

    /**
     * Metodo principal en el que realizamos los pasos a seguir por la colonia
     * de hormigas
     */
    public int[] resolver() {
        imprimirPesosyBeneficios();
        inicializarRastros();           // Paso 1.
        prepararHormigas();             // Paso 2.

        for (int i = 0; i < ITERACIONES; i++) {
            moverHormigas();            // Paso 3.
            actualizarRastros();        // Paso 4.
            actualizarMejor();          // Paso 5.
            prepararHormigas();         // Vuelvo a colocar a las hormigas en su sitio reseteando recorridos
        }

        return mejorRecorrido.clone();
    }

    /**
     * Paso 0:Inicializar todos los rastros con el mismo valor (C=1.0)
     */
    private void inicializarRastros() {
        for (int i = 0; i < numElementos; i++) {

            rastros[i] = C;

        }
    }

    /**
     * Paso 1: Preparar hormigas para la simulación, colocándolas en la ciudad
     * inicial (ciudad 0)s
     */
    private void prepararHormigas() {

        for (int i = 0; i < numHormigas; i++) {
            hormigas.get(i).resetUsados();
            hormigas.get(i).usarElemento((int) (Math.random() * 99), pesos); // Las coloco todas en un origen de los elementos
        }

    }

    /**
     * Paso 2: Mover hormigas en cada iteración: cada hormiga va construyendo su
     * vector de colores usados.
     */
    private void moverHormigas() {

        int indice = 0;

        for (Hormiga hormiga : hormigas) {
            while (!todoUsado(hormiga)) {
                indice = siguienteElemento(hormiga);
                hormiga.usarElemento(indice, pesos);
            }
        }

    }

    /**
     * Paso 3: Actualizar los rastros que las hormigas usaron
     */
    private void actualizarRastros() {
        double contribucion;
        // En primer lugar, evapora rastros en toda la matriz, con la misma tasa EVAPORACION
        for (int i = 0; i < numElementos; i++) {

            rastros[i] *= EVAPORACION;

        }

        // En segundo lugar, añadir el rastro dejado por las hormigas en los recorridos que han encontrado
        for (int i = 0; i < hormigas.size(); i++) {
            contribucion = Q / hormigas.get(i).calcularBeneficios(beneficios);
            for (int j = 0; j < numElementos; j++) {
                rastros[hormigas.get(i).recorrido[j]] += contribucion;
            }

        }
    }

    /**
     * Paso 4: Actualizar la mejor solución
     */
    private void actualizarMejor() {
        /*comprobamos la mejor solucion, si tiene mejor beneficio
        que el anterior mejor se actualiza sino no*/
        for (int i = 0; i < hormigas.size(); i++) {
            if (hormigas.get(i).calcularBeneficios(beneficios) > mejorCoste && hormigas.get(i).calcularPeso(pesos) < Capacidad) {

                mejorCoste = hormigas.get(i).calcularBeneficios(beneficios);
                mejorRecorrido = hormigas.get(i).recorrido.clone();
                mejorPeso = hormigas.get(i).calcularPeso(pesos);

            }
        }
    }

    /**
     * Método auxiliar: Seleccionar el siguiente bit para cada hormiga
     *
     * Hormiga a la que estamos buscando el siguiente elemento que le vamos a
     * poner Me devuelve el indice del elemento que vamso a cambiar
     */
    private int siguienteElemento(Hormiga hormiga) {
        Random random = new Random();
        calcularProbabilidades(hormiga); // Calculo p_k(r,s) para todos los bits candidatos en J_k(r,s)
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numElementos; i++) {
            total += probTransicion[i];
            if (total >= r) {   // Selecciono el primer color posible que cumple el criterio aleatorio
                return i;       // Devuelvo el número de color a la que voy después
            }
        }

        throw new RuntimeException("No hay otros colores");    // Para el caso en que no se ejecute ningún return
    }

    /**
     * Método auxiliar: Calcular las probabiliddes de los siguientes colores`--
     * p_k(r,s)
     *
     * @param hormiga Hormiga a la que le calculamos la probabilidad
     */
    public void calcularProbabilidades(Hormiga hormiga) {

        double denominador = 0.0;
        double numerador;
        for (int l = 0; l < numElementos; l++) { // El denominador es común a todas las ciudades
            if (!hormiga.isUsado(l)) {       // elementos candidatos que forman J_k(r)
                denominador += Math.pow(rastros[l], ALPHA) * Math.pow(1.0 / hormiga.calcularBeneficios(beneficios), BETA);
            }
        }

        for (int j = 0; j < numElementos; j++) {
            if (hormiga.isUsado(j)) {        // Si el elemento no está en J_k(r)
                probTransicion[j] = 0.0;        // La probabilidad de usarlo será 0
            } else {                            // Si no, se calcula su numerador y divide por el denominador anteriormente calculado
                numerador = Math.pow(rastros[j], ALPHA) * Math.pow(1.0 / hormiga.calcularBeneficios(beneficios), BETA);
                probTransicion[j] = numerador / denominador;
            }
        }
    }

    //mira si hemos usado todos los elementos
    public boolean todoUsado(Hormiga h) {

        boolean enc = true;
        boolean[] lista_de_usados = h.getUsados();
        int contador = 0;
        while (enc && contador < lista_de_usados.length) {
            enc = lista_de_usados[contador];
            contador++;
        }
        return enc;

    }

    //devuelve el mejor coste
    public double getMejorCoste() {
        return mejorCoste;
    }

    /**
     * imprime el recorrido
     */
    public String printRecorrido() {
        String cadena = "";
        for (int i = 0; i < mejorRecorrido.length; i++) {
            cadena += "" + mejorRecorrido[i];
        }
        return cadena;
    }

//obtiene el mejor peso
    public double getMejorPeso() {
        return mejorPeso;
    }

    //Imprime todos los pesos y benedficios que pueden introducirse en la mochila
    public void imprimirPesosyBeneficios() {
        System.out.println("\t\t\t\t TODOS LOS ELEMENTOS \t\t\t\t");
        System.out.println("\t\tELEMENTO\t\tPESO\t\t\tBENEFICIO");
        for (int i = 0; i < numElementos; i++) {
            System.out.println("\t\t" + (i + 1) + "\t\t" + pesos[i] + "\t\t" + beneficios[i]);
        }
    }

    //imprime el mejor recorrido realizado
    public void mejorRecorrido() {
        System.out.println("\t\tELEMENTO\t\tPESO\t\t\tBENEFICIO");
        for (int i = 0; i < numElementos; i++) {
            if (mejorRecorrido[i] == 1) {
                System.out.println("\t\t" + (i + 1) + "\t\t" + pesos[i] + "\t\t" + beneficios[i]);
            }
        }
    }
}
