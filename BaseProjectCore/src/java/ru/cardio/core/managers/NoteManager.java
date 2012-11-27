package ru.cardio.core.managers;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.cardio.core.jpa.entity.Note;

/**
 *
 * @author rogvold
 */
@Stateless
public class NoteManager implements NoteManagerLocal {

    @PersistenceContext(unitName = "BaseProjectCorePU")
    EntityManager em;

    @Override
    public List<Note> getSessionNotes(Long sessionId) {
        try {
            return em.createQuery("select n from Note n  where n.sessionId=:sid order by n.startPoint asc").setParameter("sid", sessionId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addNote(Long sessionId, long start, long duration, String title, String description) {
        Note note = new Note();
        note.setDescription(description);
        note.setStartPoint(start);
        note.setDuration(duration);
        note.setTitle(title);
        note.setSessionId(sessionId);
        em.merge(note);

    }

    @Override
    public void deleteNote(Long noteId) {
        System.out.println("try to delete note... noteId = " + noteId);
        try {
            Note note = em.find(Note.class, noteId);
            em.remove(note);
        } catch (Exception e) {
            System.out.println("deleteNote(): exc = " + e.toString());
        }

    }

    @Override
    public Note getNoteById(Long noteId) {
        try {
            return em.find(Note.class, noteId);
        } catch (Exception e) {
            System.out.println("getNoteById: exc = " + e.toString());
            return null;
        }
    }

    @Override
    public List<Long> getSessionNotesStartList(Long SessionId) {
        List<Note> l = getSessionNotes(SessionId);
        List<Long> list = new ArrayList();
        for (Note n : l) {
            list.add(n.getStartPoint());
        }
        return list;
    }

    @Override
    public Note getNoteByStart(long start) {
        try {
            Query q = em.createQuery("select n from Note n where n.startPoint = :st");
            q.setParameter("st", start);
            return (Note) q.getSingleResult();
        } catch (Exception e) {
             return null;
        }
    }
}
