# fabulous-number-formatter


##　使い方

### build.gradle
```
dependencies {
    compile 'com.noumi0k.fabulousnumberformatter:fabulousnumberformatter:1.0.0'
}

repositories {
    maven { url 'http://raw.github.com/noumi0k/fabulousnumberformatter/master/repository/' }
}
```

### Activity
```
import com.noumi0k.fabulous_number_formatter.FabulousNumberFormatter;

public class SampleApp implements TextWatcher {

     ~~~~~~~
     ~~~~~~~
      
    EditText editText;
    int maxDecimal = 8;
    
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        FabulousNumberFormatter.updateCommaSeparators(editable.toString(), maxDecimal, editText, this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (editText != null) {
            editText.addTextChangedListener(this);
        }
    }
 
     @Override
     protected void onPause() {
         super.onPause();
         if (editText != null) {
             editText.removeTextChangedListener(this);
         }
     }
     
     ~~~~~~~
     ~~~~~~~
}
```

