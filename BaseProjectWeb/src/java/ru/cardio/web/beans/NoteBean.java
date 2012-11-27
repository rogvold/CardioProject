package ru.cardio.web.beans;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import ru.cardio.core.jpa.entity.Note;
import ru.cardio.core.managers.NoteManagerLocal;

/**
 *
 * @author rogvold
 */
@ManagedBean
@ViewScoped
public class NoteBean implements Serializable {
    
    @EJB
    NoteManagerLocal noteMan;
    
    private Note selectedNote;
    private List<Note> selectedSessionNotesList;
    private Long selectedSessionId;

    private long selectedNoteStart;
    
    private long start;
    private long duration;
    private String description;
    private String title;
    
    public void selectSession(Long sessionId){
        this.selectedSessionId = sessionId;
        this.selectedSessionNotesList = noteMan.getSessionNotes(sessionId);
        System.out.println("selectSession selectedSessionNotesList = " + selectedSessionNotesList);
    }
    
    public void clearNoteWords(){
        this.description="";
        this.title = "";
        this.start = 0;
        this.duration = 0;
    }
    
    public void selectNote(Long noteId){
        this.selectedNote = noteMan.getNoteById(noteId);
    }
    
    public String jsStartPointsArray(Long sessionId){
        System.out.println("jsStartPointsArray sessionId = " + sessionId);
        selectSession(sessionId);
        if (selectedSessionNotesList == null || selectedSessionNotesList.isEmpty()) return "[]";
        String s = "[";
       
        for (int i=0; i < selectedSessionNotesList.size() - 1; i++){
            s+= "["+selectedSessionNotesList.get(i).getStartPoint() + ", "+ selectedSessionNotesList.get(i).getDuration() + "]" + ", ";
        }
        
        s+="["+selectedSessionNotesList.get(selectedSessionNotesList.size() - 1).getStartPoint() + ", "+ selectedSessionNotesList.get(selectedSessionNotesList.size() - 1).getDuration() + "]" +"]";
        
        return s;
    }
    
    public void initSelectedNote(ActionEvent evt){
        System.out.println("selectedNoteStart = " + selectedNoteStart);
        this.selectedNote = noteMan.getNoteByStart(selectedNoteStart);
        System.out.println("selectedNote = " + selectedNote);
    }

    
    public void deleteSelectedNote(ActionEvent evt){
        System.out.println("noteBean: deleteSelectedNote... selectedNote.getId() = " + selectedNote.getId());
        noteMan.deleteNote(selectedNote.getId());
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSelectedNoteStart() {
        return selectedNoteStart;
    }

    public void setSelectedNoteStart(long selectedNoteStart) {
        this.selectedNoteStart = selectedNoteStart;
    }

    
    
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
    
    
    
    public Note getSelectedNote() {
        System.out.println("getSelectedNote(): selectedNote = " + selectedNote);
        return selectedNote;
    }

    public void setSelectedNote(Note selectedNote) {
        this.selectedNote = selectedNote;
    }

    public Long getSelectedSessionId() {
        return selectedSessionId;
    }

    public void setSelectedSessionId(Long selectedSessionId) {
        this.selectedSessionId = selectedSessionId;
    }

    public List<Note> getSelectedSessionNotesList() {
        return selectedSessionNotesList;
    }

    public void setSelectedSessionNotesList(List<Note> selectedSessionNotesList) {
        this.selectedSessionNotesList = selectedSessionNotesList;
    }
    
    public void createNote(){
        System.out.println("creating note: start = " + start + " ; duration = " + duration + " ; title = " + title + " ; description = " + description);
        noteMan.addNote(selectedSessionId, start, duration, title, description);
    }
    
        
}
