
package colonia_de_hormigas;

public class Principal {

    public static final int NUM_ELEMENTOS = 100;

    public static void main(String[] args) {
       //creamos la colonia de hormiga 
        Colonia_de_hormigas aco = new Colonia_de_hormigas(NUM_ELEMENTOS,generar_pesos(),generar_beneficios());

        //funcion resolver
        aco.resolver();

        //imprimir todos los resultados
        System.out.println("\n\t\t\t\t ELEMENTOS SELECCIONADOS \t\t\t\t");
        aco.mejorRecorrido();
//        System.out.println("recorrido= " + aco.printRecorrido());
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