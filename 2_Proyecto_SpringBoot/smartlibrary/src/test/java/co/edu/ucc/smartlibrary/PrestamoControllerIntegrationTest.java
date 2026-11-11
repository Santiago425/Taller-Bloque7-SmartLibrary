package co.edu.ucc.smartlibrary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.edu.ucc.smartlibrary.repository.LibroRepository;
import co.edu.ucc.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    private String cuerpo(long estudianteId, long libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test
    @DisplayName("E1 - Préstamo de libro disponible: 201 Created y préstamo registrado")
    void prestamoDeLibroDisponible() throws Exception {
        long antes = prestamoRepository.count();
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.libroId").value(1))
                .andExpect(jsonPath("$.estudianteId").value(1))
                .andExpect(jsonPath("$.activo").value(true));
        assertThat(prestamoRepository.count()).isEqualTo(antes + 1);
        assertThat(libroRepository.findById(1L).orElseThrow().isDisponible()).isFalse();
    }

    @Test
    @DisplayName("E2 - Libro ya prestado: 409 Conflict sin nuevo préstamo")
    void libroYaPrestado() throws Exception {
        long antes = prestamoRepository.count();
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.estado").value(409));
        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("E3 - Estudiante inexistente: 404 Not Found")
    void estudianteInexistente() throws Exception {
        long antes = prestamoRepository.count();
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(99, 1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe el estudiante con id 99"));
        assertThat(prestamoRepository.count()).isEqualTo(antes);
        assertThat(libroRepository.findById(1L).orElseThrow().isDisponible()).isTrue();
    }

    @Test
    @DisplayName("E4 - Libro inexistente: 404 Not Found")
    void libroInexistente() throws Exception {
        long antes = prestamoRepository.count();
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 99)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe el libro con id 99"));
        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("E5 - Consulta de préstamos: 200 OK con registros")
    void consultaDePrestamos() throws Exception {
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tituloLibro").value("Patrones de Diseño"));
    }

    @Test
    @DisplayName("E6 - Solicitud inválida (R4): 400 Bad Request sin préstamo")
    void solicitudInvalida() throws Exception {
        long antes = prestamoRepository.count();
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{\"libroId\":1}"))
                .andExpect(status().isBadRequest());
        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }
}
