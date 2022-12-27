package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	public AuthDAO mockAuthDao;
	public GenericDAO mockGenericDao;
	@BeforeEach
	public void setUp(){
		mockAuthDao = Mockito.mock(AuthDAO.class);
		mockGenericDao = Mockito.mock(GenericDAO.class);
	}

	@Test
	public void testStartRemoteSystemWithValidUserAndSystem() throws Exception {
		User validUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);

		String validId = "12345";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);

		Assertions.assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
	}

	@Test
	public void testStopRemoteSystemWithValidUserAndSystem() throws Exception {
		User validUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);

		String validId = "12345";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		Collection<Object> retorno = manager.stopRemoteSystem(validUser.getId(), validId);

		Assertions.assertEquals(retorno.toString(), "[uno, dos]");
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
	}

	@Test
	public void testAddRemoteSystemWithValidUserAndSystem() throws Exception {
		User validUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);

		String validId = "12345";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList());
		when(mockGenericDao.updateSomeData(validUser, lista)).thenReturn(true);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		manager.addRemoteSystem(validUser.getId(), lista);

		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, lista);
	}

	@Test
	public void testAddRemoteSystemWithValidUserAndSystemFailling() throws Exception {
		User validUser = new User("1", "Ana", "Lopez", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);

		String validId = "12345";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList());
		when(mockGenericDao.updateSomeData(validUser, lista)).thenReturn(false);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		Assertions.assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(validUser.getId(), lista);
		}, "usuario valido pero no pudo añadir remote");

		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, lista);
	}

	@Test
	public void testdeleteRemoteSystemWithValidUserAndSystem() throws Exception {
		User auth = new User("1", "Antonio", "Perez", "Madrid", new ArrayList<Object>());
		String validId = "12345";
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList());
		when(mockGenericDao.deleteSomeData(auth, validId)).thenReturn(true);

		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		manager.deleteRemoteSystem("1", validId);
	}

}
