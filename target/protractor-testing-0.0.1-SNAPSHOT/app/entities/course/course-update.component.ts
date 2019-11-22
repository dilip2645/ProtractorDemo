import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { ToasterService, ToasterType, ToasterTitle } from 'app/shared/component/toaster';
import { FormGroup } from '@angular/forms';
import { FormType } from 'app/shared';
import { SpinnerService } from 'app/shared/component/spinner';
@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html'
})
export class CourseUpdateComponent implements OnInit {
  breadcrumb: object = {};
  isSaving: boolean;
  courseForm: FormGroup;
  formType: FormType;
  course: Course;
  panelTitle: String = '';
  editForm = this.fb.group({
    id: [],
    courseName: [],
    courseFee: [],
    courseDuration: []
  });

  constructor(
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private toaster: ToasterService,
    private spinner: SpinnerService,
    private router: Router
  ) {
    this.formType = FormType.add;
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ course }) => {
      if (course['id']) {
        this.formType = FormType.update;
        this.updateForm(course);
        this.panelTitle = 'Edit Course';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'course',
          entityEdit: 'Edit',
          entityUrl: 'course'
        };
      } else {
        this.course = new Course();
        this.panelTitle = 'Add Course';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'course',
          entityEdit: 'Add',
          entityUrl: 'course'
        };
      }
    });
  }

  updateForm(course: ICourse) {
    this.editForm.patchValue({
      id: course.id,
      courseName: course.courseName,
      courseFee: course.courseFee,
      courseDuration: course.courseDuration
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined && course.id !== null) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  private createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id']).value,
      courseName: this.editForm.get(['courseName']).value,
      courseFee: this.editForm.get(['courseFee']).value,
      courseDuration: this.editForm.get(['courseDuration']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>) {
    this.spinner.spin(true);
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }
  delete() {
    if (this.editForm.getRawValue().id !== undefined && this.editForm.getRawValue().id !== '') {
      this.subscribeToDeleteResponse(this.courseService.delete(this.editForm.getRawValue().id));
    } else {
      this.onError('Error');
    }
  }
  private subscribeToDeleteResponse(result: Observable<HttpResponse<Course>>) {
    result.subscribe((res: HttpResponse<Course>) => this.onDeleteSuccess(res.body), (res: HttpErrorResponse) => this.onError('Error'));
  }
  private onError(error: any) {
    this.spinner.spin(false);
    this.toaster.toast({
      message: error,
      type: ToasterType.Error,
      title: ToasterTitle.Error
    });
    this.isSaving = false;
  }
  private onDeleteSuccess(result: Course) {
    this.isSaving = false;
    this.toaster.toast({
      message: 'Deleted successfully',
      type: ToasterType.Success,
      title: ToasterTitle.Success
    });
    this.router.navigate(['course']);
  }
  private onSaveSuccess() {
    this.spinner.spin(false);
    this.isSaving = false;
    if (this.editForm.getRawValue().id !== undefined && this.editForm.getRawValue().id !== null) {
      this.toaster.toast({
        message: 'Updated successfully',
        type: ToasterType.Success,
        title: ToasterTitle.Success
      });
    } else {
      this.toaster.toast({
        message: 'Added successfully',
        type: ToasterType.Success,
        title: ToasterTitle.Success
      });
    }
    this.previousState();
  }

  protected onSaveError() {
    this.spinner.spin(false);
    this.isSaving = false;
  }
  closeDeleteModal(deleteModal: any) {
    deleteModal.close();
  }

  openDeleteModal(deleteModal: any) {
    deleteModal.open();
  }
}
