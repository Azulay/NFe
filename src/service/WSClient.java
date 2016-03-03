package service;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub.NfeCabecMsg;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub.NfeCabecMsgE;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub.NfeDadosMsg;

public class WSClient {
	
	public boolean callNfeAutorizacao(){
		
		try {
			
			
			
			NfeAutorizacaoStub stub = new NfeAutorizacaoStub();
			NfeDadosMsg dadosMsg = new NfeDadosMsg();
			NfeCabecMsgE cabecMsg = new NfeCabecMsgE();
			
			// Definir DadosMsg
			
			// Definir CabecMsg
			
			stub.nfeAutorizacaoLote(dadosMsg, cabecMsg);
		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
