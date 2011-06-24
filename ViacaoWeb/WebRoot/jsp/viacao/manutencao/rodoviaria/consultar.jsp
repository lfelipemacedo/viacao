<%@ include file="/jsp/common/taglibs.jsp"%>

<script type="text/javascript">
function voltar(){
var frm = document.forms[0];
	frm.task.value = 'unspecified';
	frm.submit();
}
</script>
<html:form action="/manterRodoviaria">
<html:hidden property="task" name="manterRodoviariaForm" />
	<table width="600" border="0" align="center">
		<tr>
			<td>
				<table width="100%" border="0" align="center">
					<tr>
						<td class="titulo">
							Consulta de Rodovi�ria
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" id="cadastrar"
					class="bordatabela">
					<tr class="fundoescuro">
						<td colspan="4" align="center" class="texto">
							Consulta de Rodovi�ria
						</td>
					</tr>
					<tr class="fundoclaro">
						<td class="texto" align="center" width="05%">
							Rodovi�ria
						</td>
						<td width="45%">
							<html:text disabled="true" styleClass="inputdeletar" size="20" name="manterRodoviariaForm" property="rodoviariaVO.nomRodoviaria"/>
						</td>
						<td class="texto" align="center" width="05%">
							Estado
						</td>
						<td width="45%">
							<html:text disabled="true" name="manterRodoviariaForm" size="20"  property="rodoviariaVO.enderecoVO.estado"/>							
						</td>
					</tr>
					<tr class="fundoclaro">
						<td class="texto" align="center" width="05%">
							Cidade
						</td>
						<td width="45%">
							<html:text disabled="true" name="manterRodoviariaForm" size="20" property="rodoviariaVO.enderecoVO.cidade"/>

						</td>
						<td class="texto" align="center" width="05%">
							Bairro
						</td>
						<td width="45%">
							<html:text disabled="true" name="manterRodoviariaForm" size="20" property="rodoviariaVO.enderecoVO.bairro"/>						 	
						</td>
					</tr>
					<tr class="fundoclaro">
						<td class="texto" align="center" width="05%">
							Logradouro
						</td>
						<td width="95%" colspan="3">
							<html:text disabled="true" name="manterRodoviariaForm" size="101" property="rodoviariaVO.enderecoVO.logradouro"/>

					
						</td>
					</tr>
					<tr class="fundoclaro">
						<td class="texto" align="center" width="05%">
							N�mero
						</td>
						<td width="45%">
							<html:text disabled="true" name="manterRodoviariaForm" size="5" property="rodoviariaVO.enderecoVO.numero"/>						

							
						</td>
						<td class="texto" align="center" width="05%">
							Complemento
						</td>
						<td width="45%">
							<html:text disabled="true" name="manterRodoviariaForm" size="20" property="rodoviariaVO.enderecoVO.complemento"/>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" align="center">
					<tr>
						<td align="center">
							<input type="button" class="botao" value="Voltar" onclick="voltar();">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>