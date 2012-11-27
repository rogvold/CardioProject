package ru.cardio.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.cardio.core.jpa.entity.Note;

/**
 *
 * @author rogvold
 */
@Local
public interface NoteManagerLocal {

    public Note getNoteByStart(long start);
    
    public void addNote(Long sessionId, long start, long duration, String title, String description);
    
    public List<Note> getSessionNotes(Long sessionId);
    
    public Note getNoteById(Long noteId);
    
    public void deleteNote(Long noteId);
    
    public List<Long> getSessionNotesStartList(Long SessionId);
    
}
