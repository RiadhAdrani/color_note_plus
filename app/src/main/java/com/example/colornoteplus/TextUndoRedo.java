package com.example.colornoteplus;

import java.util.Stack;

public class TextUndoRedo {

    private  Stack<String> undoStack ;
    private  Stack<String> redoStack ;
    private OnTextChange onTextChange;

    public TextUndoRedo(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public Stack<String> getUndoStack(){ return undoStack; }

    public Stack<String> getRedoStack(){ return redoStack; }

    public void pushUndo(String string){ undoStack.push(string); }

    public void pushRedo(String string){ redoStack.push(string); }

    public String popUndo(){ return undoStack.pop(); }

    public String popRedo(){ return redoStack.pop(); }

    public void updateUndo(){ onTextChange.updateRedo(); }

    public void updateRedo(){ onTextChange.updateRedo(); }

    public void setOnTextChange(OnTextChange onTextChange) { this.onTextChange = onTextChange; }

    public interface OnTextChange{
        void updateUndo();
        void updateRedo();
    }

}
