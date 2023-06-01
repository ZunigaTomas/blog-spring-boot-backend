public class Algoritmos {
    public static void main(String[] args) {


    }

    private static int sumarHastaN(int n) {
        int respuesta;

        if(n == 1) {
            return 1;
        } else {
            respuesta = n + sumarHastaN(n - 1);
        }

        return respuesta;
    }
}
