import java.util.Arrays;

public class Calculator {

    private String hidden = "";
    private Status status = new Status();

    private InputType current_operator = InputType.None;
    public InputType GetCurrentOperator() {
        return isOperator(current_operator) ? current_operator : InputType.None;
    }

    private StringBuilder display_value = new StringBuilder();

    public String GetDisplayValue() {
        if (status.Is(Status.Flag.Result_Display)) return hidden;
        else return display_value.length() > 0 ? display_value.toString() : "0";
    }

    public static InputType[] operators = { InputType.Sub, InputType.Add, InputType.Div, InputType.Eq, InputType.Mul };

    public Calculator() {

    }

    public void Input(InputType input) {
		if(input == InputType.Erase){
            Erase(false);
            return;
        }else if (input == InputType.EraseAll){
            Erase(true);
            return;
        }

        if (!isOperator(input)) {
            if (status.Is(Status.Flag.Result_Display)) {
                status.Off(Status.Flag.Result_Display);
            }

            if (status.Is(Status.Flag.Operator_Just_Pressed)) {
                display_value.setLength(0);
                status.Off(Status.Flag.Operator_Just_Pressed);
            }

            if (_isNumbers(input)) {
                _Numbers(input);
            } else {
                _SpecialSymbols(input);
            }
        } else {
            if (display_value.length() == 0) {
                display_value.append("0");
            }

            _Operate(input);
        }
    }

    private boolean _isNumbers(InputType input) {
        return input.getValue() > 0 && input.getValue() < 10;
    }
    private void _Numbers(InputType input) {
        display_value.append(input.getValue());
    }

    private void _SpecialSymbols(InputType input) {
        switch (input) {
            case Num0:
                if (display_value.length() > 0) display_value.append("0");
                break;
            case Num00:
                if (display_value.length() > 0) display_value.append("00");
                break;
            case Dot:
                if (status.Is(Status.Flag.Dotted)) break;
                if (display_value.length() > 0) {
                    display_value.append(".");
                } else {
                    display_value.append("0.");
                }
                status.On(Status.Flag.Dotted);
                break;
        }
    }

    private void _Operate(InputType input) {
        if (input == InputType.Eq) {
            _Calculate(current_operator);
        } else if(isOperator(input)){
            if (status.Is(Status.Flag.Operator_Pressed)) {
                _Calculate(current_operator);
            } else {
                if (status.IsNot(Status.Flag.Result_Display)) {
                    hidden = display_value.toString();
                }
                status.ResetExcept(Status.Flag.Result_Display);
            }
            current_operator = input;
            status.On(Status.Flag.Operator_Pressed | Status.Flag.Operator_Just_Pressed);
        }
    }

    private void _Calculate(InputType input) {
        switch (input) {
            case Add:
                hidden = String.valueOf(Double.parseDouble(hidden) + Double.parseDouble(display_value.toString()));
                break;
            case Sub:
                hidden = String.valueOf(Double.parseDouble(hidden) - Double.parseDouble(display_value.toString()));
                break;
            case Mul:
                hidden = String.valueOf(Double.parseDouble(hidden) * Double.parseDouble(display_value.toString()));
                break;
            case Div:
                hidden = String.valueOf(Double.parseDouble(hidden) / Double.parseDouble(display_value.toString()));
                break;
            default:
                return;
        }
        display_value.setLength(0);
        status.Reset();
        status.On(Status.Flag.Result_Display);
    }


    public void Erase(boolean erase_all) {
        if (erase_all || status.Is(Status.Flag.Result_Display)) {
            hidden = "";
            current_operator = InputType.None;
            status.Reset();
        }
        display_value.setLength(0);
    }

    private boolean isOperator(InputType input) {
        return Arrays.asList(operators).contains(input);
    }
}

