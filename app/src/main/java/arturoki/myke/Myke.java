package arturoki.myke;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by arturo on 13/12/2016.
 */

public class Myke extends InputMethodService  implements KeyboardView.OnKeyboardActionListener {
    private CandidateView mCandidateView;
    private KeyboardView kv;
    private Mykeview y;
    private Keyboard keyboard;
    private Keyboard keyboard2;//declaramos el keyboard aqui
    private Keyboard keyboard3;
   // private boolean caps = false;
    private boolean caps = false;
    private int estado=0;
    private int estadoshift=1;
    private StringBuilder mComposing = new StringBuilder();
    private Myke mCurKeyboard;
    private boolean mCompletionOn;
    private String mWordSeparators;


    @Override
    public View onCreateInputView() {
        SharedPreferences pref =PreferenceManager.getDefaultSharedPreferences(this);//manager
        boolean rb0 = pref.getBoolean(Preferencias.KEY, false);//guardamos en una string lo de blanco y lo referenciamos al preferencias que es donde lo tenemos
        if(rb0){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
        }else {kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);}
      //  kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboard2= new Keyboard(this,R.xml.symbol);//contexto y el xml
        keyboard3= new Keyboard(this,R.xml.symbol2);
        kv.setKeyboard(keyboard);
//
        //updateShift();

//
        kv.setOnKeyboardActionListener(this);
        return kv;
    }
   /* public int updateShift(){
es int pero realmente no necesito el return
        return estado;
    }*/
    public void updateShift(){
        if(estado==0&&estadoshift==2){
            caps=true;
        keyboard.setShifted(caps);
        kv.invalidateAllKeys();

        }else if(estado==1&&estadoshift==1){
            caps=false;
            keyboard.setShifted(caps);
            kv.invalidateAllKeys();
            estado=0;estadoshift=0;
        }else if(estado==0&&estadoshift==1){
            caps=true;
            keyboard.setShifted(caps);
            kv.invalidateAllKeys();
           // estado=0;estadoshift=0;
        }
    }
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        setInputView(onCreateInputView());
       // estadoshift=1; estado=1;
       // if(estado!=0){}//distinto
        updateShift();
        updateCandidates();
    }
    @Override
    public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(this);
        return mCandidateView;
    }/*
    @Override
    public View onCreateCandidatesView() {

        }*/
   /* @Override
    public void updateInputViewShown(){

    }*/
  /*  @Override
    public boolean onEvaluateInputViewShown(){
        SharedPreferences pref =PreferenceManager.getDefaultSharedPreferences(this);//manager
        boolean rb0 = pref.getBoolean(Preferencias.KEY, false);//guardamos en una string lo de blanco y lo referenciamos al preferencias que es donde lo tenemos
        if(rb0){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
        }else {kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);}
       return true;
    }*/
    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            /*case -2:
                if(kv.getKeyboard().equals(keyboard)){
                kv.setKeyboard(keyboard2);
                }else {
                    kv.setKeyboard(keyboard);
                }
                break;*/
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                if(estadoshift==0){estadoshift=1;caps = true;//caps = !caps; lo pongo true directamente porque me minimizaba el teclado, lo quiero entero bien
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys(); break;}
                if(estadoshift==1){estadoshift=2;caps = true;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys(); estado=0;Toast.makeText(this,"Bloq mayus",Toast.LENGTH_SHORT).show();  break;}//bloqueado
                if(estadoshift==2){estadoshift=0;caps = false;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys(); break;}//no esta
               /* caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();//hace el redraw del teclado*/
                break;
            case Keyboard.KEYCODE_CANCEL:
                     //el cancel es -3 es para que sea una tecla el otro teclado ya vere como reparo esto
                if(kv.getKeyboard().equals(keyboard2)){
                    kv.setKeyboard(keyboard3);
                }
                else {
                    kv.setKeyboard(keyboard2);
                }
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if(kv.getKeyboard().equals(keyboard)){
                    kv.setKeyboard(keyboard2);
                }else {
                    kv.setKeyboard(keyboard);
                }
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                if(estadoshift==1){estado=1; updateShift();}
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }


    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();

        mComposing.setLength(0);
        updateCandidates();

     
        setCandidatesViewShown(false);
estado=0; estadoshift=1;
       /* mCurKeyboard = mQwertyKeyboard;
        if (mInputView != null) {
            mInputView.closing();
        }*/
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                            int newSelStart, int newSelEnd,
                                            int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }
    private void updateCandidates() {
        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mComposing.toString());
               // setSuggestions(list, true, true);
            } else {
                //setSuggestions(null, false, false);
            }
        }
    }
}
