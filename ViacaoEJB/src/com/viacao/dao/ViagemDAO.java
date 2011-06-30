package com.viacao.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.acol.exception.DAOException;
import com.viacao.services.persistence.BaseDB;
import com.viacao.vo.DataVO;
import com.viacao.vo.ItinerarioVo;
import com.viacao.vo.OnibusVO;
import com.viacao.vo.RodoviariaVO;
import com.viacao.vo.ViagemVO;

public class ViagemDAO extends BaseDB {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public ViagemDAO() throws DAOException {
		super();
	}
	
	public String getSQLInserir() throws DAOException {
		StringBuffer sql = new StringBuffer();
	
		sql.append("INSERT INTO viagem ( ");
		sql.append("					seq_viagem, ");
		sql.append("					seq_itinerario_FK, ");
		sql.append("					seq_onibus_FK, ");
		sql.append("					data_hora_saida, ");
		sql.append("					data_hora_chegada) ");
		sql.append("VALUES (seq_viagem.nextval, param_seq_itinerario (?), param_seq_onibus (?)");
		sql.append("to_date 'param_data_hora_saida', 'DD/MM/YY HH24:MI:SS' (?)");
		sql.append("to_date 'param_data_hora_chegada', 'DD/MM/YY HH24:MI:SS'(?), )");
	   
		return sql.toString();
	}
	
	public void inserir(ViagemVO viagemVO)throws DAOException {
		try{
			pstmt = getPstmt(getSQLInserir());
			pstmt.setInt(1, viagemVO.getItinerarioVo().getSeqItinerario());
			pstmt.setInt(2, viagemVO.getOnibusVO().getSeqOnibus());
			pstmt.setString(3, viagemVO.getHoraSaida().getHoraMinutoSegundo());
			pstmt.setString(4, viagemVO.getHoraChegada().getHoraMinutoSegundo());
			
			pstmt.executeUpdate();
		}
		catch(SQLException e){
			logger.fatal("Erro ocorrido no metodo inserir em :: ViagemDAO", e);
		}
		finally{
			release();
		}
	}
	
	public void deletar(ViagemVO viagemVO)throws DAOException{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("DELETE viagem vg WHERE vg.seq_viagem = ? ");
		
		try{
			pstmt = getPstmt(sql.toString());
			pstmt.setInt(1, viagemVO.getSeqViagem());
			
			pstmt.executeUpdate();
		}
		catch(SQLException e){
			logger.fatal("Erro ocorrido no metodo deletar em :: ViagemDAO", e);
			throw new DAOException(e);
		}
		finally{
			release();
		}
	}
	
	public String getSQLAlterar() throws DAOException {;
		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE	viagem");
		sql.append(" SET 	seq_itinerario_FK = ? , ");
		sql.append("		seq_onibus_FK     = ? , ");
		sql.append("		data_hora_saida   = to_date('?', 'DD/MM/YYYY HH24:MI:SS'),");
		sql.append("		data_hora_chegada = to_date('?', 'DD/MM/YYYY HH24:MI:SS')");
		sql.append(" WHERE	seq_viagem = ? ");
	
		return sql.toString();
	}
	
	public void alterar(ViagemVO viagemVO)throws DAOException{
		try{
			pstmt = getPstmt(getSQLAlterar());
			pstmt.setInt(1, viagemVO.getItinerarioVo().getSeqItinerario());
			pstmt.setInt(2, viagemVO.getOnibusVO().getSeqOnibus());
			pstmt.setString(3, viagemVO.getHoraSaida().getData());
			pstmt.setString(4, viagemVO.getHoraChegada().getData());
			pstmt.setInt(5, viagemVO.getSeqViagem());
			
			pstmt.executeUpdate();
		}
		catch(SQLException e){
			logger.fatal("Erro ocorrido no metodo alterar em :: ViagemDAO", e);
			throw new DAOException(e);
		}
		finally{
			release();
		}
	}
	
	public String getSQLViagem(){
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT	vg.seq_viagem, ");
		sql.append("  		(SELECT		r.nom_rodoviaria ");
		sql.append("         	FROM	rodoviaria r ");
		sql.append("         	WHERE	r.seq_rodoviaria = it.seq_rodoviaria_origem_fk) origem, ");
		sql.append("       	(SELECT 	r.nom_rodoviaria ");
		sql.append("         	FROM	rodoviaria r ");
		sql.append("         	WHERE 	r.seq_rodoviaria = it.seq_rodoviaria_destino_fk) destino, ");
		sql.append("		o.tipo, ");
		sql.append("         to_char(vg.data_hora_saida, 'DD/MM/YYYY') data_saida, ");
		sql.append("        to_char(vg.data_hora_saida, 'HH24:MI:SS') hora_saida, ");
		sql.append("        to_char(vg.data_hora_chegada, 'DD/MM/YYYY') data_chegada, ");
		sql.append("        to_char(vg.data_hora_chegada, 'HH24:MI:SS') hora_chegada ");
		sql.append(" FROM	itinerario it, viagem vg, onibus o ");
		sql.append(" WHERE	it.seq_itinerario = vg.seq_itinerario_fk ");
		sql.append(" AND	o.seq_onibus = vg.seq_onibus_fk");
		sql.append(" AND 	vg.seq_viagem = ? ");
		
		return sql.toString();
	}
	
	public ViagemVO getViagem (ViagemVO viagemVO)throws DAOException, Exception{
		try{
			pstmt = getPstmt(getSQLViagem());
			pstmt.setInt(1, viagemVO.getSeqViagem().intValue());
			rowSet = executeQuery(pstmt);
			
			ViagemVO vgVO = new ViagemVO();
			if(rowSet.next()){
				vgVO.setSeqViagem(new Integer(rowSet.getInt("seq_viagem")));
				vgVO.setItinerarioVo(new ItinerarioVo());
				vgVO.getItinerarioVo().setRodoviariaOrigemVO(new RodoviariaVO());
				vgVO.getItinerarioVo().setRodoviariaDestinoVO(new RodoviariaVO());
				vgVO.getItinerarioVo().getRodoviariaOrigemVO().setNomRodoviaria(rowSet.getString("origem"));
				vgVO.getItinerarioVo().getRodoviariaDestinoVO().setNomRodoviaria(rowSet.getString("destino"));
				vgVO.setOnibusVO(new OnibusVO());
				vgVO.getOnibusVO().setTipo(rowSet.getString("tipo"));
				vgVO.setHoraSaida(new DataVO(rowSet.getString("data_saida")));
				vgVO.getHoraSaida().setHoraMinutoSegundo(rowSet.getString("hora_saida"));
				vgVO.setHoraChegada(new DataVO(rowSet.getString("data_chegada")));
				vgVO.getHoraChegada().setHoraMinutoSegundo(rowSet.getString("hora_chegada"));
			}
			return vgVO;
		}
		catch (SQLException e) {
			logger.fatal("Erro ocorrido no metodo getViagem em :: ViagemDAO", e);
			throw new DAOException(e);
		}
		finally{
			release();
		}
	}
	
	private String getSQLListaViagem(ViagemVO vgVO){
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT  vg.seq_viagem, ");
		sql.append(" 	    (SELECT		r.nom_rodoviaria ");
		sql.append("         	FROM 	rodoviaria r");
		sql.append("         	WHERE 	r.seq_rodoviaria = it.seq_rodoviaria_origem_fk) origem, ");
		sql.append("        (SELECT 	r.nom_rodoviaria ");
		sql.append("         	FROM 	rodoviaria r ");
		sql.append("         	WHERE 	r.seq_rodoviaria = it.seq_rodoviaria_destino_fk) destino, ");
		sql.append("        o.tipo, ");
		sql.append("        to_char(vg.data_hora_saida, 'DD/MM/YYYY') data_saida, ");
		sql.append("        to_char(vg.data_hora_saida, 'HH24:MI:SS') hora_saida, ");
		sql.append("        to_char(vg.data_hora_chegada, 'DD/MM/YYYY') data_chegada, ");
		sql.append("        to_char(vg.data_hora_chegada, 'HH24:MI:SS') hora_chegada ");
		sql.append(" FROM 	itinerario it, viagem vg, onibus o ");
		sql.append(" WHERE 	it.seq_itinerario = vg.seq_itinerario_fk (+) ");
		sql.append(" AND 	o.seq_onibus = vg.seq_onibus_fk ");
		// filtro por origem da viagem
		if(vgVO.getItinerarioVo() != null){
			sql.append(" AND (SELECT r.nom_rodoviaria FROM rodoviaria r WHERE r.seq_rodoviaria = it.seq_rodoviaria_origem_fk) LIKE upper('%" + vgVO.getItinerarioVo().getRodoviariaOrigemVO().getNomRodoviaria() + "%') ");
		}
		// filtro por destino da viagem
		if(vgVO.getItinerarioVo() != null){
			sql.append(" AND (SELECT r.nom_rodoviaria FROM rodoviaria r WHERE r.seq_rodoviaria = it.seq_rodoviaria_destino_fk) LIKE upper('%" + vgVO.getItinerarioVo().getRodoviariaDestinoVO().getNomRodoviaria() + "%') ");
		}
		// filtro por tipo de �nibus
		if(vgVO.getOnibusVO() != null){
			sql.append(" AND (SELECT o.tipo FROM onibus o WHERE o.seq_onibus = vg.seq_onibus_fk) LIKE upper ('%"+ vgVO.getOnibusVO().getTipo() +"%') ");
		}
		// filtro por data partida
		if(vgVO.getHoraSaida() != null){
			sql.append(" AND to_char(vg.data_hora_saida, 'DD/MM/YYYY') = '"+ vgVO.getHoraSaida().getData() +"' ");
		}
		// filtro por hora partida
		if(vgVO.getHoraSaida() != null){
			sql.append(" AND to_char(vg.data_hora_saida, 'HH24:MI:SS')= '"+ vgVO.getHoraSaida().getHoraMinutoSegundo() +"' ");
		}
		
		
		return sql.toString();
	}
	
	public List< ViagemVO > getListaViagem (ViagemVO viagemVO)throws Exception, DAOException{
		try{
			pstmt = getPstmt(getSQLListaViagem(viagemVO));
			
			rowSet = executeQuery(pstmt);
			List<ViagemVO> lista = new ArrayList<ViagemVO>();
			
			while(rowSet.next()){
				ViagemVO viagem = new ViagemVO();
				viagem.setSeqViagem(new Integer(rowSet.getInt("seq_viagem")));
				viagem.setItinerarioVo(new ItinerarioVo());
				viagem.getItinerarioVo().setRodoviariaOrigemVO(new RodoviariaVO());
				viagem.getItinerarioVo().setRodoviariaDestinoVO(new RodoviariaVO());
				viagem.getItinerarioVo().getRodoviariaOrigemVO().setNomRodoviaria(rowSet.getString("origem"));
				viagem.getItinerarioVo().getRodoviariaDestinoVO().setNomRodoviaria(rowSet.getString("destino"));
				viagem.setOnibusVO(new OnibusVO());
				viagem.getOnibusVO().setTipo(rowSet.getString("tipo"));
				viagem.setHoraSaida(new DataVO(rowSet.getString("data_saida")));
				viagem.getHoraSaida().setHoraMinutoSegundo(rowSet.getString("hora_saida"));
				viagem.setHoraChegada(new DataVO(rowSet.getString("data_chegada")));
				viagem.getHoraChegada().setHoraMinutoSegundo(rowSet.getString("hora_chegada"));
				lista.add(viagem);
			}
			return lista;
		}
		catch (SQLException e) {
			logger.fatal("Erro ocorrido no metodo listaViagem em :: ViagemDAO");
			throw new DAOException(e);
		}
		finally{
			release();
		}
	}

	public CachedRowSet executeQuery(PreparedStatement p) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
