/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prestamo_libro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrestamoLibro.findAll", query = "SELECT p FROM PrestamoLibro p")
    , @NamedQuery(name = "PrestamoLibro.findByIdPrestamoLibro", query = "SELECT p FROM PrestamoLibro p WHERE p.idPrestamoLibro = :idPrestamoLibro")
    , @NamedQuery(name = "PrestamoLibro.findByFechaDesde", query = "SELECT p FROM PrestamoLibro p WHERE p.fechaDesde = :fechaDesde")
    , @NamedQuery(name = "PrestamoLibro.findByFechaHasta", query = "SELECT p FROM PrestamoLibro p WHERE p.fechaHasta = :fechaHasta")})
public class PrestamoLibro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_prestamo_libro")
    private Integer idPrestamoLibro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "id_libro", referencedColumnName = "id_libro")
    @ManyToOne
    private Libro idLibro;

    public PrestamoLibro() {
    }

    public PrestamoLibro(Integer idPrestamoLibro) {
        this.idPrestamoLibro = idPrestamoLibro;
    }

    public PrestamoLibro(Integer idPrestamoLibro, Date fechaDesde, Date fechaHasta) {
        this.idPrestamoLibro = idPrestamoLibro;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public Integer getIdPrestamoLibro() {
        return idPrestamoLibro;
    }

    public void setIdPrestamoLibro(Integer idPrestamoLibro) {
        this.idPrestamoLibro = idPrestamoLibro;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Libro getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Libro idLibro) {
        this.idLibro = idLibro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrestamoLibro != null ? idPrestamoLibro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrestamoLibro)) {
            return false;
        }
        PrestamoLibro other = (PrestamoLibro) object;
        if ((this.idPrestamoLibro == null && other.idPrestamoLibro != null) || (this.idPrestamoLibro != null && !this.idPrestamoLibro.equals(other.idPrestamoLibro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PrestamoLibro[ idPrestamoLibro=" + idPrestamoLibro + " ]";
    }
    
}
