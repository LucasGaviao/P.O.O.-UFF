public class Estadia{
    private String cliente;
    private int quarto;
    private String entrada;
    private String saida;
    private boolean checkin;

    public Estadia(String cliente, int quarto, String entrada, String saida){
        this.cliente = cliente;
        this.quarto = quarto;
        this.entrada = entrada;
        this.saida = saida;
        this.checkin = false;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getQuarto() {
        return quarto;
    }

    public void setQuarto(int quarto) {
        this.quarto = quarto;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }
}
