package com.example.vaxtrax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    Button submit;
    RequestQueue reqQ;
    String baseUrl = "http://10.0.2.2:8080";
    String quizOne = "/quizOne";
    String quizTwo = "/quizTwo";
    ArrayList<AnsOneModel> ansOneList = new ArrayList<>();
    String stringArrOne;
    ArrayList<AnsTwoModel> ansTwoList = new ArrayList<>();
    String stringArrTwo;
    boolean reqOneResolved = false;
    boolean reqTwoResolved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        submit = findViewById(R.id.btnSubmit);

        reqQ = Volley.newRequestQueue(this);

        for (int i = 1; i < 5; i++) {
            AnsOneModel ansOne = new AnsOneModel(i, false, false, false, false);
            AnsTwoModel ansTwo = new AnsTwoModel(i, false, false);
            ansOneList.add(ansOne);
            ansTwoList.add(ansTwo);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (allAnswered()) {
                        try {
                            JSONArray arrOne = new JSONArray();
                            JSONArray arrTwo = new JSONArray();
                            for (AnsOneModel obj : ansOneList) {
                                JSONObject o = new JSONObject();
                                o.put("questionId", obj.getQuestionId());
                                o.put("optionOne", obj.isOptionOne());
                                o.put("optionTwo", obj.isOptionTwo());
                                o.put("optionThree", obj.isOptionThree());
                                o.put("optionFour", obj.isOptionFour());
                                arrOne.put(o);
                            }
                            for (AnsTwoModel obj : ansTwoList) {
                                JSONObject o = new JSONObject();
                                o.put("questionId", obj.getQuestionId());
                                o.put("optionOne", obj.isOptionOne());
                                o.put("optionTwo", obj.isOptionTwo());
                                arrTwo.put(o);
                            }
                            stringArrOne = arrOne.toString();
                            stringArrTwo = arrTwo.toString();
                            postAnsOne();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(QuizActivity.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "No internet connectivity found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean allAnswered() {
        boolean listOne = true;
        boolean listTwo = true;
        for (AnsOneModel obj : ansOneList) {
            if (!obj.isOptionOne() && !obj.isOptionTwo() && !obj.isOptionThree() && !obj.isOptionFour()) {
                listOne = false;
                break;
            }
        }
        for (AnsTwoModel obj : ansTwoList) {
            if (!obj.isOptionOne() && !obj.isOptionTwo()) {
                listTwo = false;
                break;
            }
        }
        return listOne && listTwo;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void postAnsOne() {
        StringRequest req = new StringRequest(Request.Method.PUT, baseUrl + quizOne,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TAG", "onResponse: " + response);
                        reqOneResolved = true;
                        postAnsTwo();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", stringArrOne);
                Log.i("TAG", "getParams: " + params.toString());
                return params;
            }
        };
        reqQ.add(req);
    }

    public void postAnsTwo() {
        StringRequest req = new StringRequest(Request.Method.PUT, baseUrl + quizTwo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("TAG", "onResponse: " + response);
                        reqTwoResolved = true;
                        if (reqOneResolved && reqTwoResolved) {
                            Toast.makeText(QuizActivity.this, "Quiz Submitted", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", stringArrTwo);
                Log.i("TAG", "getParams: " + params.toString());
                return params;
            }
        };
        reqQ.add(req);
    }

    public void handleSelection(int questionId, String selection, boolean isMult) {
        if (isMult) {
            // update ans list one
            for (AnsOneModel obj : ansOneList) {
                if (obj.getQuestionId() == questionId) {
                    if (selection.equalsIgnoreCase("A")) {
                        obj.setOptionOne(true);
                        obj.setOptionTwo(false);
                        obj.setOptionThree(false);
                        obj.setOptionFour(false);
                    } else if (selection.equalsIgnoreCase("B")) {
                        obj.setOptionOne(false);
                        obj.setOptionTwo(true);
                        obj.setOptionThree(false);
                        obj.setOptionFour(false);
                    } else if (selection.equalsIgnoreCase("C")) {
                        obj.setOptionOne(false);
                        obj.setOptionTwo(false);
                        obj.setOptionThree(true);
                        obj.setOptionFour(false);
                    } else if (selection.equalsIgnoreCase("D")) {
                        obj.setOptionOne(false);
                        obj.setOptionTwo(false);
                        obj.setOptionThree(false);
                        obj.setOptionFour(true);
                    }
                }
            }
        } else {
            // update ans list two
            for (AnsTwoModel obj : ansTwoList) {
                if (obj.getQuestionId() == questionId) {
                    if (selection.equalsIgnoreCase("A")) {
                        obj.setOptionOne(true);
                        obj.setOptionTwo(false);
                    } else if (selection.equalsIgnoreCase("B")) {
                        obj.setOptionOne(false);
                        obj.setOptionTwo(true);
                    }
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rdgb1a:
                if (checked) {
                    handleSelection(1, "A", true);
                    break;
                }
            case R.id.rdgb1b:
                if (checked) {
                    handleSelection(1, "B", true);
                    break;
                }
            case R.id.rdgb1c:
                if (checked) {
                    handleSelection(1, "C", true);
                    break;
                }
            case R.id.rdgb1d:
                if (checked) {
                    handleSelection(1, "D", true);
                    break;
                }
            case R.id.rdgb2a:
                if (checked) {
                    handleSelection(2, "A", true);
                    break;
                }
            case R.id.rdgb2b:
                if (checked) {
                    handleSelection(2, "B", true);
                    break;
                }
            case R.id.rdgb2c:
                if (checked) {
                    handleSelection(2, "C", true);
                    break;
                }
            case R.id.rdgb2d:
                if (checked) {
                    handleSelection(2, "D", true);
                    break;
                }
            case R.id.rdgb3a:
                if (checked) {
                    handleSelection(3, "A", true);
                    break;
                }
            case R.id.rdgb3b:
                if (checked) {
                    handleSelection(3, "B", true);
                    break;
                }
            case R.id.rdgb3c:
                if (checked) {
                    handleSelection(3, "C", true);
                    break;
                }
            case R.id.rdgb3d:
                if (checked) {
                    handleSelection(3, "D", true);
                    break;
                }
            case R.id.rdgb4a:
                if (checked) {
                    handleSelection(4, "A", true);
                    break;
                }
            case R.id.rdgb4b:
                if (checked) {
                    handleSelection(4, "B", true);
                    break;
                }
            case R.id.rdgb4c:
                if (checked) {
                    handleSelection(4, "C", true);
                    break;
                }
            case R.id.rdgb4d:
                if (checked) {
                    handleSelection(4, "D", true);
                    break;
                }
            case R.id.rdgb5a:
                if (checked) {
                    handleSelection(1, "A", false);
                    break;
                }
            case R.id.rdgb5b:
                if (checked) {
                    handleSelection(1, "B", false);
                    break;
                }
            case R.id.rdgb6a:
                if (checked) {
                    handleSelection(2, "A", false);
                    break;
                }
            case R.id.rdgb6b:
                if (checked) {
                    handleSelection(2, "B", false);
                    break;
                }
            case R.id.rdgb7a:
                if (checked) {
                    handleSelection(3, "A", false);
                    break;
                }
            case R.id.rdgb7b:
                if (checked) {
                    handleSelection(3, "B", false);
                    break;
                }
            case R.id.rdgb8a:
                if (checked) {
                    handleSelection(4, "A", false);
                    break;
                }
            case R.id.rdgb8b:
                if (checked) {
                    handleSelection(4, "B", false);
                    break;
                }
        }
    }
}