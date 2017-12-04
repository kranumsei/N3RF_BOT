package role.season;


import java.util.List;
import java.util.Stack;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class MembroDAO {
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("n3rfPU");

	public static void adicionaNovoRegistro(String id, String nome) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Membro membro = new Membro(id, nome, Long.parseLong("0"), Long.parseLong("0"), 1);
		em.persist(membro);
		tx.commit();
		em.close();
	}

	public static Membro buscar(String id) {
		
		EntityManager em = emf.createEntityManager();
		if(em.find(Membro.class, id) != null) {
			return em.find(Membro.class, id);
		}else {
			return null;
		}
	}

	public static void atualizarPontosTemp(String id, Long valor) {
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Membro membroAtual = buscar(id);
		if(membroAtual != null) {
			tx.begin();
			membroAtual.setPontosTemp(valor);
			em.merge(membroAtual);
			tx.commit();
			em.close();
		}
		
	}

	public static void atualizarPontosTotais(String id) {
		
		long pontosTotais = calculaPontosTotais(id);
		long pontosAtuais = getPontosAtuais(id);
		
		if (pontosAtuais != 0) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			Membro membroAtual = buscar(id);
			tx.begin();
			if(membroAtual != null) {
				membroAtual.setPontosTotais(pontosTotais);
				membroAtual.setPontosTemp(Long.parseLong("0"));
				em.merge(membroAtual);
			}
			tx.commit();
			em.close();
		} else {
			return;
		}
	}

	public static long calculaPontosTotais(String id) {
		int modificador = getModifier(id);
		Membro membroAtual = buscar(id);
		long pontuacaoInicial = membroAtual.getPontosTemp();
		long pontuacaoTotal = membroAtual.getPontosTotais();
		long result;
		if (modificador == 2) {
			result = pontuacaoTotal + (((System.currentTimeMillis() / 1000) - pontuacaoInicial) / 60);
		} else if (modificador == 3) {
			result = pontuacaoTotal + (((System.currentTimeMillis() / 1000) - pontuacaoInicial) / 20);
		} else if (modificador == 4) {
			result = pontuacaoTotal + (((System.currentTimeMillis() / 1000) - pontuacaoInicial) / 10);
		} else {
			result = pontuacaoTotal + (((System.currentTimeMillis() / 1000) - pontuacaoInicial) / 300);
		}
		return result;
	}

	public static void setModifier(String id, Integer modificador) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Membro membroAtual = buscar(id);
		if(membroAtual != null) {
			tx.begin();
			membroAtual.setModificador(modificador);
			em.merge(membroAtual);
			tx.commit();
			em.close();
		}
		
	}

	public static int getModifier(String id) {
		Membro membroAtual = buscar(id);
		if(membroAtual.getModifier() >= 0) {
			return membroAtual.getModifier();
		}else {
			return -1;
		}
	}

	public static long getPontosTotais(String id) {
		Membro membroAtual = buscar(id);
		if(membroAtual.getPontosTotais() > 0) {
			return membroAtual.getPontosTotais();
		}else {
			return 0;
		}
	}

	public static long getPontosAtuais(String id) {
		Membro membroAtual = buscar(id);
		if(membroAtual.getPontosTemp() > 0) {
			return membroAtual.getPontosTemp();
		}else {
			return 0;
		}
	}

	public static boolean exists(String id) {
		Membro membroAtual = buscar(id);
		if(membroAtual == null) {
			return false;
		}else {
			return true;
		}
	}

	public static void resetRanking() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		String hql="delete from Membro m";
		Query query=em.createQuery(hql);
		tx.begin();
		query.executeUpdate();
		tx.commit();
		em.close();
	}

	@SuppressWarnings("unchecked")
	public static String ordenarRanking() {
		String resposta = "";
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("n3rfPU");
		EntityManager em = emf.createEntityManager();
		String hql="from Membro m order by m.pontosTotais + 0 desc";
		Query query=em.createQuery(hql);
		List<Membro> membrosLista = query.getResultList();
		for(int i = 0; i < membrosLista.size(); i++) {
			String nome = membrosLista.get(i).getNome();
			Long pontos = membrosLista.get(i).getPontosTotais();
			resposta = resposta + nome + " ------ " + pontos + "\n";
		}
		em.close();
		return resposta;
	}

	public static Stack<Long> rankingStack() {
		Stack<Long> ids = new Stack<>();
		EntityManager em = emf.createEntityManager();
		String hql="select id from Membro order by pontosTotais + 0 asc";
		Query query= em.createQuery(hql);
		List<String> lista = query.getResultList();
		for(int i = 0; i < lista.size(); i++) {
			ids.push(Long.parseLong(lista.get(i)));
		}
		return ids;
	}
}
