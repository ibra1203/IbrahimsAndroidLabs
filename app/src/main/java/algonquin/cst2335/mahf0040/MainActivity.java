package algonquin.cst2335.mahf0040;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This class is used to test the password complexity inputted by the user and
 *  either allow or deny access using toast messages.
 * @author Ibrahim Mahfouz
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {


    /** This holds the text at the centre of the screen*/
    TextView tv = null;

    /** This is where the password is inputted by user*/
    EditText et = null;

    /** This is the login button used to verify to password*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.etPassField);
         btn = findViewById(R.id.btnLogin);


        btn.setOnClickListener( clk -> {
            String password = et.getText().toString();

            if (checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });



    }


    /** this function is used to check if inputted password is valid
     *
     * @param password the string object that is being checked
     * @return true if password is complex enough
     */
     boolean checkPasswordComplexity(String password) {

         boolean foundUpperCase =false , foundLowerCase = false, foundNumber = false, foundSpecial=false;

         //foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;


         char a;

         // loop through each element
         for(int i = 0; i<password.length(); i++) {

             // access each character
             a = password.charAt(i);

             if (Character.isDigit(a)) {
                 foundNumber = true;
             }

             if (Character.isUpperCase(a)){
                 foundUpperCase = true;
             }

             if (Character.isLowerCase(a)){
                 foundLowerCase = true;
             }
             if (isSpecialCharacter(a)){
                 foundSpecial = true;
             }

         }

             if(!foundUpperCase)
             {

                 // Say that they are missing an upper case letter;
                 Toast.makeText(getApplicationContext(),"Upper case letter is missing",Toast.LENGTH_SHORT).show();

                 return false;

             }

             else if( ! foundLowerCase)
             {
                 // Say that they are missing a lower case letter;
                 Toast.makeText(getApplicationContext(),"Lower case letter is missing",Toast.LENGTH_LONG).show();

                 return false;

             }

             else if( ! foundNumber) {

                 Toast.makeText(getApplicationContext(),"Number is missing",Toast.LENGTH_LONG).show();
             }

             else if(! foundSpecial) {
                 Toast.makeText(getApplicationContext(),"Special character is missing",Toast.LENGTH_LONG).show();

             }

             else

                 //only get here if they're all true
                 return true;




         return false;

     }


    /** switch case statement used to ensure a select few special characters can be used
     * @param c
     * @return
     */
    boolean isSpecialCharacter(char c)
    {
        switch (c)
        {
            case '#':
            case '?':
            case '$':
            case '%':
            case '^':
            case '@':
            case '!':
            case '&':
                return true;
            default:
                return false;

        }

    }
}