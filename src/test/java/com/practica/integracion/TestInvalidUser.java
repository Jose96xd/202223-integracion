package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {


	public AuthDAO mockAuthDao;
	public GenericDAO mockGenericDao;
	@BeforeEach
	public void setUp(){
		mockAuthDao = Mockito.mock(AuthDAO.class);
		mockGenericDao = Mockito.mock(GenericDAO.class);
	}

	@Test
	public void testStartRemoteSystemWithInvalidUserAndSystem() throws Exception {
		User invalidUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);

		String invalidId = "12345";
		when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(OperationNotSupportedException.class);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		Assertions.assertThrows(SystemManagerException.class, () -> {
			manager.startRemoteSystem(invalidUser.getId(), invalidId);
		}, "usuario invalido debería dar excepción");
	}

	@Test
	public void testStopRemoteSystemWithInvalidUserAndSystem() throws Exception {
		User invalidUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);

		String invalidId = "12345";
		when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(OperationNotSupportedException.class);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		Assertions.assertThrows(SystemManagerException.class, () -> {
			manager.stopRemoteSystem(invalidUser.getId(), invalidId);
		}, "usuario invalido debería dar excepción");
	}

	@Test
	public void testAddRemoteSystemWithInvalidUserAndSystem() throws Exception {
		User invalidUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);

		ArrayList<Object> lista = new ArrayList<>();
		when(mockGenericDao.updateSomeData(null, lista)).thenThrow(OperationNotSupportedException.class);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		Assertions.assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(invalidUser.getId(), lista);
		}, "usuario invalido, da excepción");

		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(null, lista);
	}


}
