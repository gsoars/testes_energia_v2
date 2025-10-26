package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Unidade {

    @Expose(serialize = false)
    private int id;

    @Expose(serialize = false)
    private String nome;

    @Expose(serialize = false)
    private String endereco;

    @Expose(serialize = false)
    private String tipo;

    @Expose(serialize = false)
    private Contrato contrato;
}