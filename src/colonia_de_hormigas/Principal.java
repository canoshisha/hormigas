
package colonia_de_hormigas;

public class Principal {

    public static final int NUM_ELEMENTOS = 100;

    public static void main(String[] args) {
       //creamos la colonia de hormiga 
        Colonia_de_hormigas aco = null;

        //funcion resolver
        long t_inicial, t_final;
        long t_total = 0;
        for (int i = 0; i < 30; i++) {
            aco = new Colonia_de_hormigas(NUM_ELEMENTOS,generar_pesos(),generar_beneficios());
            t_inicial = System.currentTimeMillis();
            aco.resolver(); 
            t_final = System.currentTimeMillis();
            t_total += t_final - t_inicial;
        }
        System.out.println("Tiempo de ejecución de media: " + (t_total / 30)/1000.0 + " sec.");
        aco.imprimirPesosyBeneficios();
        System.out.println("recorrido= " + aco.printRecorrido());//representacion binaria
        //imprimir todos los resultados
        System.out.println("\n\t\t\t\t ELEMENTOS SELECCIONADOS \t\t\t\t");
        aco.mejorRecorrido();
        System.out.println("\n\t\t\t\t BENEFICIO Y PESO TOTAL \t\t\t\t");
        System.out.println("\n\t\t\t\tBeneficio= " + aco.getMejorCoste());
        System.out.println("\n\t\t\t\tPeso= " + aco.getMejorPeso());

    }
    
    private static double[] generar_pesos() {
        double[] pesos = new double[NUM_ELEMENTOS];
        for (int i = 0; i < NUM_ELEMENTOS; i++) {
            pesos[i] = (Math.random() * 100 + 1);
        }
        return pesos;
    }
    
    private static double[] generar_beneficios() {
        double[] beneficios = new double[NUM_ELEMENTOS];
        for (int i = 0; i < NUM_ELEMENTOS; i++) {
            beneficios[i] = (Math.random() * 100 + 1);
        }
        return beneficios;
    }
}