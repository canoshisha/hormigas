package colonia_de_hormigas;

public class Hormiga {
    private int capacidad = 3000;
    /**
     * Numero de bits que tiene nuestra solucion
     */
    protected int numElementos;

    /**
     * Va almacenando los bits
     */
    protected int[] recorrido;

    /**
     * Vector binario que controla si se usado un bit o no
     */
    protected boolean[] usados;      // 

    public Hormiga(int numElementos) {
        this.numElementos = numElementos;
        this.recorrido = new int[numElementos];
        this.usados = new boolean[numElementos];
    }

    public int getNumElementos() {
        return numElementos;
    }

    public void setNumElementos(int numElementos) {
        this.numElementos = numElementos;
    }

    public int[] getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(int[] recorrido) {
        this.recorrido = recorrido;
    }

    public boolean[] getUsados() {
        return usados;
    }

    public void setUsados(boolean[] usados) {
        this.usados = usados;
    }

    /**
     * Metodo que usa un elemento e indica que lo hemos usado
     */
    protected void usarElemento(int indice, double[] pesos) {
        recorrido[indice] = 1;
        if (calcularPeso(pesos) > capacidad) {
            recorrido[indice] = 0;
        }
        usados[indice] = true;
    }

    protected boolean isUsado(int i) {
        return usados[i];
    }

    /**
     * calculamos el beneficio de la hormiga
     */
    protected double calcularBeneficios(double[] beneficios) {
        double beneficio = 0;
        for (int i = 0; i < numElementos; i++) {

            beneficio += recorrido[i] * beneficios[i];

        }
        return beneficio;

    }

    /**
     * calculamos el peso de la hormiga
     */
    protected double calcularPeso(double[] pesos) {
        double peso = 0;
        for (int i = 0; i < numElementos; i++) {

            peso += recorrido[i] * pesos[i];

        }
        return peso;
    }

    /**
     * Metodo que me resetea el vector usados de una hormiga
     */
    protected void resetUsados() {
        for (int i = 0; i < numElementos; i++) {
            usados[i] = false;
        }
    }
}
