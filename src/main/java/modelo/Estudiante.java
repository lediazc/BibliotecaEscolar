package modelo;

public class Estudiante extends Persona {

    private String curso;


    // Constructor
    public Estudiante(int id, String nombre, String rut, String correo, String curso) {

        super(id, nombre, rut, correo);
        this.curso = curso;
    }

    // Getter
    public String getCurso() {
        return curso;
    }

    // Setter
    public void setCurso(String curso) {
        this.curso = curso;
    }

    // Implementación del metodo abstracto de Persona
    @Override
    public String getTipoPersona() {
        return "ESTUDIANTE";
    }
}
