import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { IPersonDetails, PersonDetails } from 'app/shared/model/person-details.model';
import { PersonDetailsService } from './person-details.service';
import { ToasterService, ToasterType, ToasterTitle } from 'app/shared/component/toaster';
import { FormGroup } from '@angular/forms';
import { FormType, SpinnerService } from 'app/shared';
@Component({
  selector: 'jhi-person-details-update',
  templateUrl: './person-details-update.component.html'
})
export class PersonDetailsUpdateComponent implements OnInit {
  breadcrumb: object = {};
  isSaving: boolean;
  personDetailsForm: FormGroup;
  formType: FormType;
  personDetails: PersonDetails;
  panelTitle: String = '';
  editForm = this.fb.group({
    id: [],
    personName: [],
    personId: [null, [Validators.required]],
    personAddress: []
  });

  constructor(
    protected personDetailsService: PersonDetailsService,
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
    this.activatedRoute.data.subscribe(({ personDetails }) => {
      if (personDetails['id']) {
        this.formType = FormType.update;
        this.updateForm(personDetails);
        this.panelTitle = 'Edit PersonDetails';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'person-details',
          entityEdit: 'Edit',
          entityUrl: 'person-details'
        };
      } else {
        this.personDetails = new PersonDetails();
        this.panelTitle = 'Add PersonDetails';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'person-details',
          entityEdit: 'Add',
          entityUrl: 'person-details'
        };
      }
    });
  }

  updateForm(personDetails: IPersonDetails) {
    this.editForm.patchValue({
      id: personDetails.id,
      personName: personDetails.personName,
      personId: personDetails.personId,
      personAddress: personDetails.personAddress
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const personDetails = this.createFromForm();
    if (personDetails.id !== undefined && personDetails.id !== null) {
      this.subscribeToSaveResponse(this.personDetailsService.update(personDetails));
    } else {
      this.subscribeToSaveResponse(this.personDetailsService.create(personDetails));
    }
  }

  private createFromForm(): IPersonDetails {
    return {
      ...new PersonDetails(),
      id: this.editForm.get(['id']).value,
      personName: this.editForm.get(['personName']).value,
      personId: this.editForm.get(['personId']).value,
      personAddress: this.editForm.get(['personAddress']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonDetails>>) {
    this.spinner.spin(true);
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }
  delete() {
    if (this.editForm.getRawValue().id !== undefined && this.editForm.getRawValue().id !== '') {
      this.subscribeToDeleteResponse(this.personDetailsService.delete(this.editForm.getRawValue().id));
    } else {
      this.onError('Error');
    }
  }
  private subscribeToDeleteResponse(result: Observable<HttpResponse<PersonDetails>>) {
    result.subscribe(
      (res: HttpResponse<PersonDetails>) => this.onDeleteSuccess(res.body),
      (res: HttpErrorResponse) => this.onError('Error')
    );
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
  private onDeleteSuccess(result: PersonDetails) {
    this.isSaving = false;
    this.toaster.toast({
      message: 'Deleted successfully',
      type: ToasterType.Success,
      title: ToasterTitle.Success
    });
    this.router.navigate(['person-details']);
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
