create_database() {
  echo "Creating database..."
  if [ -e std.dat ]; then
    echo "Database already exists."
  else
    touch std.dat
    echo "Database created successfully."
  fi
}

view_database() {
  echo "Viewing database..."
  if [ -e std.dat ]; then
    cat std.dat
  else
    echo "Database not created."
  fi
}

add_student() {
  echo "Enter Student Name:"
  read name
  echo "Enter Student ID:"
  read id
  echo "Enter Student Age:"
  read age
  echo "Enter Student Marks:"
  read marks
  echo "$name | $id | $age | $marks" >> std.dat
  echo "Student added successfully!"
}

delete_student() {
  echo "Enter Student ID to delete:"
  read student_id
  if [ -e std.dat ]; then
    if grep -q "| $student_id |" std.dat; then
      grep -v "| $student_id |" std.dat > temp.dat && mv temp.dat std.dat
      echo "Student with ID $student_id has been deleted."
    else
      echo "Student with ID $student_id not found."
    fi
  else
    echo "Database not found."
  fi
}

search_student() {
  echo "Enter Student ID to search:"
  read student_id
  if [ -e std.dat ]; then
    result=$(grep "| $student_id |" std.dat)
    if [ -n "$result" ]; then
      echo "$result"
    else
      echo "Student with ID $student_id not found."
    fi
  else
    echo "Database not found."
  fi
}

modify_student() {
  echo "Enter Student ID to modify:"
  read student_id
  if [ -e std.dat ]; then
    if grep -q "| $student_id |" std.dat; then
      grep -v "| $student_id |" std.dat > temp.dat
      echo "Enter new Student Name:"
      read new_name
      echo "Enter new Student Age:"
      read new_age
      echo "Enter new Student Marks:"
      read new_marks
      echo "$new_name | $student_id | $new_age | $new_marks" >> temp.dat
      mv temp.dat std.dat
      echo "Student record updated successfully!"
    else
      echo "Student with ID $student_id not found."
    fi
  else
    echo "Database not found."
  fi
}

while true; do
  echo "Student Database Management"
  echo "1. Create Database"
  echo "2. View Database"
  echo "3. Add Student"
  echo "4. Delete Student"
  echo "5. Search Student"
  echo "6. Modify Student"
  echo "7. Exit"
  echo "Enter your choice:"
  read choice

  case $choice in
    1) create_database ;;
    2) view_database ;;
    3) add_student ;;
    4) delete_student ;;
    5) search_student ;;
    6) modify_student ;;
    7) echo "Exiting program..."; exit ;;
    *) echo "Invalid choice! Please enter a valid option." ;;
  esac
done
